package com.blitz.account.web.rest;

import static com.blitz.account.domain.ServiceLogAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.ServiceLog;
import com.blitz.account.repository.ServiceLogRepository;
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
 * Integration tests for the {@link ServiceLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceLogResourceIT {

    private static final Long DEFAULT_VEHICLE_ID = 1L;
    private static final Long UPDATED_VEHICLE_ID = 2L;

    private static final Instant DEFAULT_SERVICE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SERVICE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);

    private static final Double DEFAULT_MILEAGE_AT_SERVICE = 1D;
    private static final Double UPDATED_MILEAGE_AT_SERVICE = 2D;

    private static final Long DEFAULT_SUPPLIER_ID = 1L;
    private static final Long UPDATED_SUPPLIER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/service-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceLogRepository serviceLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceLogMockMvc;

    private ServiceLog serviceLog;

    private ServiceLog insertedServiceLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceLog createEntity() {
        return new ServiceLog()
            .vehicleId(DEFAULT_VEHICLE_ID)
            .serviceDate(DEFAULT_SERVICE_DATE)
            .description(DEFAULT_DESCRIPTION)
            .cost(DEFAULT_COST)
            .mileageAtService(DEFAULT_MILEAGE_AT_SERVICE)
            .supplierId(DEFAULT_SUPPLIER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceLog createUpdatedEntity() {
        return new ServiceLog()
            .vehicleId(UPDATED_VEHICLE_ID)
            .serviceDate(UPDATED_SERVICE_DATE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .mileageAtService(UPDATED_MILEAGE_AT_SERVICE)
            .supplierId(UPDATED_SUPPLIER_ID);
    }

    @BeforeEach
    void initTest() {
        serviceLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedServiceLog != null) {
            serviceLogRepository.delete(insertedServiceLog);
            insertedServiceLog = null;
        }
    }

    @Test
    @Transactional
    void createServiceLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceLog
        var returnedServiceLog = om.readValue(
            restServiceLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceLog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceLog.class
        );

        // Validate the ServiceLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertServiceLogUpdatableFieldsEquals(returnedServiceLog, getPersistedServiceLog(returnedServiceLog));

        insertedServiceLog = returnedServiceLog;
    }

    @Test
    @Transactional
    void createServiceLogWithExistingId() throws Exception {
        // Create the ServiceLog with an existing ID
        serviceLog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceLog)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVehicleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceLog.setVehicleId(null);

        // Create the ServiceLog, which fails.

        restServiceLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServiceDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceLog.setServiceDate(null);

        // Create the ServiceLog, which fails.

        restServiceLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceLogs() throws Exception {
        // Initialize the database
        insertedServiceLog = serviceLogRepository.saveAndFlush(serviceLog);

        // Get all the serviceLogList
        restServiceLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceDate").value(hasItem(DEFAULT_SERVICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].mileageAtService").value(hasItem(DEFAULT_MILEAGE_AT_SERVICE)))
            .andExpect(jsonPath("$.[*].supplierId").value(hasItem(DEFAULT_SUPPLIER_ID.intValue())));
    }

    @Test
    @Transactional
    void getServiceLog() throws Exception {
        // Initialize the database
        insertedServiceLog = serviceLogRepository.saveAndFlush(serviceLog);

        // Get the serviceLog
        restServiceLogMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceLog.getId().intValue()))
            .andExpect(jsonPath("$.vehicleId").value(DEFAULT_VEHICLE_ID.intValue()))
            .andExpect(jsonPath("$.serviceDate").value(DEFAULT_SERVICE_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)))
            .andExpect(jsonPath("$.mileageAtService").value(DEFAULT_MILEAGE_AT_SERVICE))
            .andExpect(jsonPath("$.supplierId").value(DEFAULT_SUPPLIER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingServiceLog() throws Exception {
        // Get the serviceLog
        restServiceLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceLog() throws Exception {
        // Initialize the database
        insertedServiceLog = serviceLogRepository.saveAndFlush(serviceLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceLog
        ServiceLog updatedServiceLog = serviceLogRepository.findById(serviceLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceLog are not directly saved in db
        em.detach(updatedServiceLog);
        updatedServiceLog
            .vehicleId(UPDATED_VEHICLE_ID)
            .serviceDate(UPDATED_SERVICE_DATE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .mileageAtService(UPDATED_MILEAGE_AT_SERVICE)
            .supplierId(UPDATED_SUPPLIER_ID);

        restServiceLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedServiceLog))
            )
            .andExpect(status().isOk());

        // Validate the ServiceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceLogToMatchAllProperties(updatedServiceLog);
    }

    @Test
    @Transactional
    void putNonExistingServiceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceLog.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceLogWithPatch() throws Exception {
        // Initialize the database
        insertedServiceLog = serviceLogRepository.saveAndFlush(serviceLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceLog using partial update
        ServiceLog partialUpdatedServiceLog = new ServiceLog();
        partialUpdatedServiceLog.setId(serviceLog.getId());

        partialUpdatedServiceLog
            .vehicleId(UPDATED_VEHICLE_ID)
            .serviceDate(UPDATED_SERVICE_DATE)
            .cost(UPDATED_COST)
            .supplierId(UPDATED_SUPPLIER_ID);

        restServiceLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceLog))
            )
            .andExpect(status().isOk());

        // Validate the ServiceLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceLog, serviceLog),
            getPersistedServiceLog(serviceLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceLogWithPatch() throws Exception {
        // Initialize the database
        insertedServiceLog = serviceLogRepository.saveAndFlush(serviceLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceLog using partial update
        ServiceLog partialUpdatedServiceLog = new ServiceLog();
        partialUpdatedServiceLog.setId(serviceLog.getId());

        partialUpdatedServiceLog
            .vehicleId(UPDATED_VEHICLE_ID)
            .serviceDate(UPDATED_SERVICE_DATE)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .mileageAtService(UPDATED_MILEAGE_AT_SERVICE)
            .supplierId(UPDATED_SUPPLIER_ID);

        restServiceLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceLog))
            )
            .andExpect(status().isOk());

        // Validate the ServiceLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceLogUpdatableFieldsEquals(partialUpdatedServiceLog, getPersistedServiceLog(partialUpdatedServiceLog));
    }

    @Test
    @Transactional
    void patchNonExistingServiceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceLog() throws Exception {
        // Initialize the database
        insertedServiceLog = serviceLogRepository.saveAndFlush(serviceLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceLog
        restServiceLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceLogRepository.count();
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

    protected ServiceLog getPersistedServiceLog(ServiceLog serviceLog) {
        return serviceLogRepository.findById(serviceLog.getId()).orElseThrow();
    }

    protected void assertPersistedServiceLogToMatchAllProperties(ServiceLog expectedServiceLog) {
        assertServiceLogAllPropertiesEquals(expectedServiceLog, getPersistedServiceLog(expectedServiceLog));
    }

    protected void assertPersistedServiceLogToMatchUpdatableProperties(ServiceLog expectedServiceLog) {
        assertServiceLogAllUpdatablePropertiesEquals(expectedServiceLog, getPersistedServiceLog(expectedServiceLog));
    }
}
