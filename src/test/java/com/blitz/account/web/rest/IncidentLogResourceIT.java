package com.blitz.account.web.rest;

import static com.blitz.account.domain.IncidentLogAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.IncidentLog;
import com.blitz.account.domain.enumeration.IncidentType;
import com.blitz.account.repository.IncidentLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IncidentLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IncidentLogResourceIT {

    private static final Long DEFAULT_VEHICLE_ID = 1L;
    private static final Long UPDATED_VEHICLE_ID = 2L;

    private static final Long DEFAULT_TRIP_ID = 1L;
    private static final Long UPDATED_TRIP_ID = 2L;

    private static final Instant DEFAULT_INCIDENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INCIDENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final IncidentType DEFAULT_TYPE = IncidentType.ACCIDENT;
    private static final IncidentType UPDATED_TYPE = IncidentType.DENT;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/incident-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IncidentLogRepository incidentLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncidentLogMockMvc;

    private IncidentLog incidentLog;

    private IncidentLog insertedIncidentLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncidentLog createEntity() {
        return new IncidentLog()
            .vehicleId(DEFAULT_VEHICLE_ID)
            .tripId(DEFAULT_TRIP_ID)
            .incidentDate(DEFAULT_INCIDENT_DATE)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .cost(DEFAULT_COST);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncidentLog createUpdatedEntity() {
        return new IncidentLog()
            .vehicleId(UPDATED_VEHICLE_ID)
            .tripId(UPDATED_TRIP_ID)
            .incidentDate(UPDATED_INCIDENT_DATE)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST);
    }

    @BeforeEach
    void initTest() {
        incidentLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIncidentLog != null) {
            incidentLogRepository.delete(insertedIncidentLog);
            insertedIncidentLog = null;
        }
    }

    @Test
    @Transactional
    void createIncidentLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IncidentLog
        var returnedIncidentLog = om.readValue(
            restIncidentLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incidentLog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IncidentLog.class
        );

        // Validate the IncidentLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIncidentLogUpdatableFieldsEquals(returnedIncidentLog, getPersistedIncidentLog(returnedIncidentLog));

        insertedIncidentLog = returnedIncidentLog;
    }

    @Test
    @Transactional
    void createIncidentLogWithExistingId() throws Exception {
        // Create the IncidentLog with an existing ID
        incidentLog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncidentLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incidentLog)))
            .andExpect(status().isBadRequest());

        // Validate the IncidentLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVehicleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        incidentLog.setVehicleId(null);

        // Create the IncidentLog, which fails.

        restIncidentLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incidentLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIncidentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        incidentLog.setIncidentDate(null);

        // Create the IncidentLog, which fails.

        restIncidentLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incidentLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIncidentLogs() throws Exception {
        // Initialize the database
        insertedIncidentLog = incidentLogRepository.saveAndFlush(incidentLog);

        // Get all the incidentLogList
        restIncidentLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incidentLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].tripId").value(hasItem(DEFAULT_TRIP_ID.intValue())))
            .andExpect(jsonPath("$.[*].incidentDate").value(hasItem(DEFAULT_INCIDENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))));
    }

    @Test
    @Transactional
    void getIncidentLog() throws Exception {
        // Initialize the database
        insertedIncidentLog = incidentLogRepository.saveAndFlush(incidentLog);

        // Get the incidentLog
        restIncidentLogMockMvc
            .perform(get(ENTITY_API_URL_ID, incidentLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incidentLog.getId().intValue()))
            .andExpect(jsonPath("$.vehicleId").value(DEFAULT_VEHICLE_ID.intValue()))
            .andExpect(jsonPath("$.tripId").value(DEFAULT_TRIP_ID.intValue()))
            .andExpect(jsonPath("$.incidentDate").value(DEFAULT_INCIDENT_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)));
    }

    @Test
    @Transactional
    void getNonExistingIncidentLog() throws Exception {
        // Get the incidentLog
        restIncidentLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIncidentLog() throws Exception {
        // Initialize the database
        insertedIncidentLog = incidentLogRepository.saveAndFlush(incidentLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incidentLog
        IncidentLog updatedIncidentLog = incidentLogRepository.findById(incidentLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIncidentLog are not directly saved in db
        em.detach(updatedIncidentLog);
        updatedIncidentLog
            .vehicleId(UPDATED_VEHICLE_ID)
            .tripId(UPDATED_TRIP_ID)
            .incidentDate(UPDATED_INCIDENT_DATE)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST);

        restIncidentLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIncidentLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIncidentLog))
            )
            .andExpect(status().isOk());

        // Validate the IncidentLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIncidentLogToMatchAllProperties(updatedIncidentLog);
    }

    @Test
    @Transactional
    void putNonExistingIncidentLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidentLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncidentLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incidentLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incidentLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncidentLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncidentLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidentLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncidentLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incidentLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncidentLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncidentLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidentLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncidentLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incidentLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncidentLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncidentLogWithPatch() throws Exception {
        // Initialize the database
        insertedIncidentLog = incidentLogRepository.saveAndFlush(incidentLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incidentLog using partial update
        IncidentLog partialUpdatedIncidentLog = new IncidentLog();
        partialUpdatedIncidentLog.setId(incidentLog.getId());

        partialUpdatedIncidentLog.vehicleId(UPDATED_VEHICLE_ID).tripId(UPDATED_TRIP_ID).type(UPDATED_TYPE).cost(UPDATED_COST);

        restIncidentLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncidentLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncidentLog))
            )
            .andExpect(status().isOk());

        // Validate the IncidentLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncidentLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIncidentLog, incidentLog),
            getPersistedIncidentLog(incidentLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateIncidentLogWithPatch() throws Exception {
        // Initialize the database
        insertedIncidentLog = incidentLogRepository.saveAndFlush(incidentLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incidentLog using partial update
        IncidentLog partialUpdatedIncidentLog = new IncidentLog();
        partialUpdatedIncidentLog.setId(incidentLog.getId());

        partialUpdatedIncidentLog
            .vehicleId(UPDATED_VEHICLE_ID)
            .tripId(UPDATED_TRIP_ID)
            .incidentDate(UPDATED_INCIDENT_DATE)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST);

        restIncidentLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncidentLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncidentLog))
            )
            .andExpect(status().isOk());

        // Validate the IncidentLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncidentLogUpdatableFieldsEquals(partialUpdatedIncidentLog, getPersistedIncidentLog(partialUpdatedIncidentLog));
    }

    @Test
    @Transactional
    void patchNonExistingIncidentLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidentLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncidentLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, incidentLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(incidentLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncidentLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncidentLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidentLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncidentLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(incidentLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncidentLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncidentLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidentLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncidentLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(incidentLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncidentLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncidentLog() throws Exception {
        // Initialize the database
        insertedIncidentLog = incidentLogRepository.saveAndFlush(incidentLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the incidentLog
        restIncidentLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, incidentLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return incidentLogRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected IncidentLog getPersistedIncidentLog(IncidentLog incidentLog) {
        return incidentLogRepository.findById(incidentLog.getId()).orElseThrow();
    }

    protected void assertPersistedIncidentLogToMatchAllProperties(IncidentLog expectedIncidentLog) {
        assertIncidentLogAllPropertiesEquals(expectedIncidentLog, getPersistedIncidentLog(expectedIncidentLog));
    }

    protected void assertPersistedIncidentLogToMatchUpdatableProperties(IncidentLog expectedIncidentLog) {
        assertIncidentLogAllUpdatablePropertiesEquals(expectedIncidentLog, getPersistedIncidentLog(expectedIncidentLog));
    }
}
