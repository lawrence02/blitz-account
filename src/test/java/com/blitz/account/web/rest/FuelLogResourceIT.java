package com.blitz.account.web.rest;

import static com.blitz.account.domain.FuelLogAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.FuelLog;
import com.blitz.account.repository.FuelLogRepository;
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
 * Integration tests for the {@link FuelLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FuelLogResourceIT {

    private static final Long DEFAULT_VEHICLE_ID = 1L;
    private static final Long UPDATED_VEHICLE_ID = 2L;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_FUEL_VOLUME = 1D;
    private static final Double UPDATED_FUEL_VOLUME = 2D;

    private static final BigDecimal DEFAULT_FUEL_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_FUEL_COST = new BigDecimal(2);

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Long DEFAULT_TRIP_ID = 1L;
    private static final Long UPDATED_TRIP_ID = 2L;

    private static final String ENTITY_API_URL = "/api/fuel-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FuelLogRepository fuelLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFuelLogMockMvc;

    private FuelLog fuelLog;

    private FuelLog insertedFuelLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FuelLog createEntity() {
        return new FuelLog()
            .vehicleId(DEFAULT_VEHICLE_ID)
            .date(DEFAULT_DATE)
            .fuelVolume(DEFAULT_FUEL_VOLUME)
            .fuelCost(DEFAULT_FUEL_COST)
            .location(DEFAULT_LOCATION)
            .tripId(DEFAULT_TRIP_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FuelLog createUpdatedEntity() {
        return new FuelLog()
            .vehicleId(UPDATED_VEHICLE_ID)
            .date(UPDATED_DATE)
            .fuelVolume(UPDATED_FUEL_VOLUME)
            .fuelCost(UPDATED_FUEL_COST)
            .location(UPDATED_LOCATION)
            .tripId(UPDATED_TRIP_ID);
    }

    @BeforeEach
    void initTest() {
        fuelLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFuelLog != null) {
            fuelLogRepository.delete(insertedFuelLog);
            insertedFuelLog = null;
        }
    }

    @Test
    @Transactional
    void createFuelLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FuelLog
        var returnedFuelLog = om.readValue(
            restFuelLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuelLog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FuelLog.class
        );

        // Validate the FuelLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFuelLogUpdatableFieldsEquals(returnedFuelLog, getPersistedFuelLog(returnedFuelLog));

        insertedFuelLog = returnedFuelLog;
    }

    @Test
    @Transactional
    void createFuelLogWithExistingId() throws Exception {
        // Create the FuelLog with an existing ID
        fuelLog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuelLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuelLog)))
            .andExpect(status().isBadRequest());

        // Validate the FuelLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVehicleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fuelLog.setVehicleId(null);

        // Create the FuelLog, which fails.

        restFuelLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuelLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fuelLog.setDate(null);

        // Create the FuelLog, which fails.

        restFuelLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuelLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFuelLogs() throws Exception {
        // Initialize the database
        insertedFuelLog = fuelLogRepository.saveAndFlush(fuelLog);

        // Get all the fuelLogList
        restFuelLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fuelLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].fuelVolume").value(hasItem(DEFAULT_FUEL_VOLUME)))
            .andExpect(jsonPath("$.[*].fuelCost").value(hasItem(sameNumber(DEFAULT_FUEL_COST))))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].tripId").value(hasItem(DEFAULT_TRIP_ID.intValue())));
    }

    @Test
    @Transactional
    void getFuelLog() throws Exception {
        // Initialize the database
        insertedFuelLog = fuelLogRepository.saveAndFlush(fuelLog);

        // Get the fuelLog
        restFuelLogMockMvc
            .perform(get(ENTITY_API_URL_ID, fuelLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fuelLog.getId().intValue()))
            .andExpect(jsonPath("$.vehicleId").value(DEFAULT_VEHICLE_ID.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.fuelVolume").value(DEFAULT_FUEL_VOLUME))
            .andExpect(jsonPath("$.fuelCost").value(sameNumber(DEFAULT_FUEL_COST)))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.tripId").value(DEFAULT_TRIP_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFuelLog() throws Exception {
        // Get the fuelLog
        restFuelLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFuelLog() throws Exception {
        // Initialize the database
        insertedFuelLog = fuelLogRepository.saveAndFlush(fuelLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fuelLog
        FuelLog updatedFuelLog = fuelLogRepository.findById(fuelLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFuelLog are not directly saved in db
        em.detach(updatedFuelLog);
        updatedFuelLog
            .vehicleId(UPDATED_VEHICLE_ID)
            .date(UPDATED_DATE)
            .fuelVolume(UPDATED_FUEL_VOLUME)
            .fuelCost(UPDATED_FUEL_COST)
            .location(UPDATED_LOCATION)
            .tripId(UPDATED_TRIP_ID);

        restFuelLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFuelLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFuelLog))
            )
            .andExpect(status().isOk());

        // Validate the FuelLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFuelLogToMatchAllProperties(updatedFuelLog);
    }

    @Test
    @Transactional
    void putNonExistingFuelLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fuelLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuelLogMockMvc
            .perform(put(ENTITY_API_URL_ID, fuelLog.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuelLog)))
            .andExpect(status().isBadRequest());

        // Validate the FuelLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFuelLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fuelLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fuelLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the FuelLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFuelLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fuelLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuelLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FuelLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFuelLogWithPatch() throws Exception {
        // Initialize the database
        insertedFuelLog = fuelLogRepository.saveAndFlush(fuelLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fuelLog using partial update
        FuelLog partialUpdatedFuelLog = new FuelLog();
        partialUpdatedFuelLog.setId(fuelLog.getId());

        partialUpdatedFuelLog.date(UPDATED_DATE).fuelVolume(UPDATED_FUEL_VOLUME).location(UPDATED_LOCATION);

        restFuelLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuelLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFuelLog))
            )
            .andExpect(status().isOk());

        // Validate the FuelLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFuelLogUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFuelLog, fuelLog), getPersistedFuelLog(fuelLog));
    }

    @Test
    @Transactional
    void fullUpdateFuelLogWithPatch() throws Exception {
        // Initialize the database
        insertedFuelLog = fuelLogRepository.saveAndFlush(fuelLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fuelLog using partial update
        FuelLog partialUpdatedFuelLog = new FuelLog();
        partialUpdatedFuelLog.setId(fuelLog.getId());

        partialUpdatedFuelLog
            .vehicleId(UPDATED_VEHICLE_ID)
            .date(UPDATED_DATE)
            .fuelVolume(UPDATED_FUEL_VOLUME)
            .fuelCost(UPDATED_FUEL_COST)
            .location(UPDATED_LOCATION)
            .tripId(UPDATED_TRIP_ID);

        restFuelLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuelLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFuelLog))
            )
            .andExpect(status().isOk());

        // Validate the FuelLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFuelLogUpdatableFieldsEquals(partialUpdatedFuelLog, getPersistedFuelLog(partialUpdatedFuelLog));
    }

    @Test
    @Transactional
    void patchNonExistingFuelLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fuelLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuelLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fuelLog.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fuelLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the FuelLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFuelLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fuelLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fuelLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the FuelLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFuelLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fuelLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fuelLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FuelLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFuelLog() throws Exception {
        // Initialize the database
        insertedFuelLog = fuelLogRepository.saveAndFlush(fuelLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fuelLog
        restFuelLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, fuelLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fuelLogRepository.count();
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

    protected FuelLog getPersistedFuelLog(FuelLog fuelLog) {
        return fuelLogRepository.findById(fuelLog.getId()).orElseThrow();
    }

    protected void assertPersistedFuelLogToMatchAllProperties(FuelLog expectedFuelLog) {
        assertFuelLogAllPropertiesEquals(expectedFuelLog, getPersistedFuelLog(expectedFuelLog));
    }

    protected void assertPersistedFuelLogToMatchUpdatableProperties(FuelLog expectedFuelLog) {
        assertFuelLogAllUpdatablePropertiesEquals(expectedFuelLog, getPersistedFuelLog(expectedFuelLog));
    }
}
