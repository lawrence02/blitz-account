package com.blitz.account.web.rest;

import static com.blitz.account.domain.FleetTripLocationAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.FleetTripLocation;
import com.blitz.account.repository.FleetTripLocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link FleetTripLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FleetTripLocationResourceIT {

    private static final Long DEFAULT_FLEET_TRIP_ID = 1L;
    private static final Long UPDATED_FLEET_TRIP_ID = 2L;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Double DEFAULT_SPEED = 1D;
    private static final Double UPDATED_SPEED = 2D;

    private static final Double DEFAULT_HEADING = 1D;
    private static final Double UPDATED_HEADING = 2D;

    private static final String ENTITY_API_URL = "/api/fleet-trip-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FleetTripLocationRepository fleetTripLocationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFleetTripLocationMockMvc;

    private FleetTripLocation fleetTripLocation;

    private FleetTripLocation insertedFleetTripLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FleetTripLocation createEntity() {
        return new FleetTripLocation()
            .fleetTripId(DEFAULT_FLEET_TRIP_ID)
            .timestamp(DEFAULT_TIMESTAMP)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .speed(DEFAULT_SPEED)
            .heading(DEFAULT_HEADING);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FleetTripLocation createUpdatedEntity() {
        return new FleetTripLocation()
            .fleetTripId(UPDATED_FLEET_TRIP_ID)
            .timestamp(UPDATED_TIMESTAMP)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .speed(UPDATED_SPEED)
            .heading(UPDATED_HEADING);
    }

    @BeforeEach
    void initTest() {
        fleetTripLocation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFleetTripLocation != null) {
            fleetTripLocationRepository.delete(insertedFleetTripLocation);
            insertedFleetTripLocation = null;
        }
    }

    @Test
    @Transactional
    void createFleetTripLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FleetTripLocation
        var returnedFleetTripLocation = om.readValue(
            restFleetTripLocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTripLocation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FleetTripLocation.class
        );

        // Validate the FleetTripLocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFleetTripLocationUpdatableFieldsEquals(returnedFleetTripLocation, getPersistedFleetTripLocation(returnedFleetTripLocation));

        insertedFleetTripLocation = returnedFleetTripLocation;
    }

    @Test
    @Transactional
    void createFleetTripLocationWithExistingId() throws Exception {
        // Create the FleetTripLocation with an existing ID
        fleetTripLocation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFleetTripLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTripLocation)))
            .andExpect(status().isBadRequest());

        // Validate the FleetTripLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFleetTripIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fleetTripLocation.setFleetTripId(null);

        // Create the FleetTripLocation, which fails.

        restFleetTripLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTripLocation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fleetTripLocation.setTimestamp(null);

        // Create the FleetTripLocation, which fails.

        restFleetTripLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTripLocation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fleetTripLocation.setLatitude(null);

        // Create the FleetTripLocation, which fails.

        restFleetTripLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTripLocation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fleetTripLocation.setLongitude(null);

        // Create the FleetTripLocation, which fails.

        restFleetTripLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTripLocation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFleetTripLocations() throws Exception {
        // Initialize the database
        insertedFleetTripLocation = fleetTripLocationRepository.saveAndFlush(fleetTripLocation);

        // Get all the fleetTripLocationList
        restFleetTripLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fleetTripLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].fleetTripId").value(hasItem(DEFAULT_FLEET_TRIP_ID.intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].speed").value(hasItem(DEFAULT_SPEED)))
            .andExpect(jsonPath("$.[*].heading").value(hasItem(DEFAULT_HEADING)));
    }

    @Test
    @Transactional
    void getFleetTripLocation() throws Exception {
        // Initialize the database
        insertedFleetTripLocation = fleetTripLocationRepository.saveAndFlush(fleetTripLocation);

        // Get the fleetTripLocation
        restFleetTripLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, fleetTripLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fleetTripLocation.getId().intValue()))
            .andExpect(jsonPath("$.fleetTripId").value(DEFAULT_FLEET_TRIP_ID.intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.speed").value(DEFAULT_SPEED))
            .andExpect(jsonPath("$.heading").value(DEFAULT_HEADING));
    }

    @Test
    @Transactional
    void getNonExistingFleetTripLocation() throws Exception {
        // Get the fleetTripLocation
        restFleetTripLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFleetTripLocation() throws Exception {
        // Initialize the database
        insertedFleetTripLocation = fleetTripLocationRepository.saveAndFlush(fleetTripLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fleetTripLocation
        FleetTripLocation updatedFleetTripLocation = fleetTripLocationRepository.findById(fleetTripLocation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFleetTripLocation are not directly saved in db
        em.detach(updatedFleetTripLocation);
        updatedFleetTripLocation
            .fleetTripId(UPDATED_FLEET_TRIP_ID)
            .timestamp(UPDATED_TIMESTAMP)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .speed(UPDATED_SPEED)
            .heading(UPDATED_HEADING);

        restFleetTripLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFleetTripLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFleetTripLocation))
            )
            .andExpect(status().isOk());

        // Validate the FleetTripLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFleetTripLocationToMatchAllProperties(updatedFleetTripLocation);
    }

    @Test
    @Transactional
    void putNonExistingFleetTripLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTripLocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFleetTripLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fleetTripLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fleetTripLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the FleetTripLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFleetTripLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTripLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetTripLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fleetTripLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the FleetTripLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFleetTripLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTripLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetTripLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTripLocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FleetTripLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFleetTripLocationWithPatch() throws Exception {
        // Initialize the database
        insertedFleetTripLocation = fleetTripLocationRepository.saveAndFlush(fleetTripLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fleetTripLocation using partial update
        FleetTripLocation partialUpdatedFleetTripLocation = new FleetTripLocation();
        partialUpdatedFleetTripLocation.setId(fleetTripLocation.getId());

        partialUpdatedFleetTripLocation
            .timestamp(UPDATED_TIMESTAMP)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .heading(UPDATED_HEADING);

        restFleetTripLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFleetTripLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFleetTripLocation))
            )
            .andExpect(status().isOk());

        // Validate the FleetTripLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFleetTripLocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFleetTripLocation, fleetTripLocation),
            getPersistedFleetTripLocation(fleetTripLocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateFleetTripLocationWithPatch() throws Exception {
        // Initialize the database
        insertedFleetTripLocation = fleetTripLocationRepository.saveAndFlush(fleetTripLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fleetTripLocation using partial update
        FleetTripLocation partialUpdatedFleetTripLocation = new FleetTripLocation();
        partialUpdatedFleetTripLocation.setId(fleetTripLocation.getId());

        partialUpdatedFleetTripLocation
            .fleetTripId(UPDATED_FLEET_TRIP_ID)
            .timestamp(UPDATED_TIMESTAMP)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .speed(UPDATED_SPEED)
            .heading(UPDATED_HEADING);

        restFleetTripLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFleetTripLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFleetTripLocation))
            )
            .andExpect(status().isOk());

        // Validate the FleetTripLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFleetTripLocationUpdatableFieldsEquals(
            partialUpdatedFleetTripLocation,
            getPersistedFleetTripLocation(partialUpdatedFleetTripLocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFleetTripLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTripLocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFleetTripLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fleetTripLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fleetTripLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the FleetTripLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFleetTripLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTripLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetTripLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fleetTripLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the FleetTripLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFleetTripLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTripLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetTripLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fleetTripLocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FleetTripLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFleetTripLocation() throws Exception {
        // Initialize the database
        insertedFleetTripLocation = fleetTripLocationRepository.saveAndFlush(fleetTripLocation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fleetTripLocation
        restFleetTripLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, fleetTripLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fleetTripLocationRepository.count();
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

    protected FleetTripLocation getPersistedFleetTripLocation(FleetTripLocation fleetTripLocation) {
        return fleetTripLocationRepository.findById(fleetTripLocation.getId()).orElseThrow();
    }

    protected void assertPersistedFleetTripLocationToMatchAllProperties(FleetTripLocation expectedFleetTripLocation) {
        assertFleetTripLocationAllPropertiesEquals(expectedFleetTripLocation, getPersistedFleetTripLocation(expectedFleetTripLocation));
    }

    protected void assertPersistedFleetTripLocationToMatchUpdatableProperties(FleetTripLocation expectedFleetTripLocation) {
        assertFleetTripLocationAllUpdatablePropertiesEquals(
            expectedFleetTripLocation,
            getPersistedFleetTripLocation(expectedFleetTripLocation)
        );
    }
}
