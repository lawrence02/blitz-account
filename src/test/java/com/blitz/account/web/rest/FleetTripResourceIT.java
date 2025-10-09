package com.blitz.account.web.rest;

import static com.blitz.account.domain.FleetTripAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.FleetTrip;
import com.blitz.account.repository.FleetTripRepository;
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
 * Integration tests for the {@link FleetTripResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FleetTripResourceIT {

    private static final Long DEFAULT_VEHICLE_ID = 1L;
    private static final Long UPDATED_VEHICLE_ID = 2L;
    private static final Long SMALLER_VEHICLE_ID = 1L - 1L;

    private static final Long DEFAULT_DRIVER_ID = 1L;
    private static final Long UPDATED_DRIVER_ID = 2L;
    private static final Long SMALLER_DRIVER_ID = 1L - 1L;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_DISTANCE_KM = 1D;
    private static final Double UPDATED_DISTANCE_KM = 2D;
    private static final Double SMALLER_DISTANCE_KM = 1D - 1D;

    private static final String DEFAULT_START_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_START_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_END_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_END_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_LOAD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LOAD_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAD_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LOAD_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ROUTE_GEO_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_ROUTE_GEO_COORDINATES = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TRIP_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRIP_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRIP_COST = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/fleet-trips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FleetTripRepository fleetTripRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFleetTripMockMvc;

    private FleetTrip fleetTrip;

    private FleetTrip insertedFleetTrip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FleetTrip createEntity() {
        return new FleetTrip()
            .vehicleId(DEFAULT_VEHICLE_ID)
            .driverId(DEFAULT_DRIVER_ID)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .distanceKm(DEFAULT_DISTANCE_KM)
            .startLocation(DEFAULT_START_LOCATION)
            .endLocation(DEFAULT_END_LOCATION)
            .loadType(DEFAULT_LOAD_TYPE)
            .loadDescription(DEFAULT_LOAD_DESCRIPTION)
            .routeGeoCoordinates(DEFAULT_ROUTE_GEO_COORDINATES)
            .tripCost(DEFAULT_TRIP_COST);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FleetTrip createUpdatedEntity() {
        return new FleetTrip()
            .vehicleId(UPDATED_VEHICLE_ID)
            .driverId(UPDATED_DRIVER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .distanceKm(UPDATED_DISTANCE_KM)
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .loadType(UPDATED_LOAD_TYPE)
            .loadDescription(UPDATED_LOAD_DESCRIPTION)
            .routeGeoCoordinates(UPDATED_ROUTE_GEO_COORDINATES)
            .tripCost(UPDATED_TRIP_COST);
    }

    @BeforeEach
    void initTest() {
        fleetTrip = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFleetTrip != null) {
            fleetTripRepository.delete(insertedFleetTrip);
            insertedFleetTrip = null;
        }
    }

    @Test
    @Transactional
    void createFleetTrip() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FleetTrip
        var returnedFleetTrip = om.readValue(
            restFleetTripMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTrip)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FleetTrip.class
        );

        // Validate the FleetTrip in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFleetTripUpdatableFieldsEquals(returnedFleetTrip, getPersistedFleetTrip(returnedFleetTrip));

        insertedFleetTrip = returnedFleetTrip;
    }

    @Test
    @Transactional
    void createFleetTripWithExistingId() throws Exception {
        // Create the FleetTrip with an existing ID
        fleetTrip.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFleetTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTrip)))
            .andExpect(status().isBadRequest());

        // Validate the FleetTrip in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVehicleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fleetTrip.setVehicleId(null);

        // Create the FleetTrip, which fails.

        restFleetTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTrip)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fleetTrip.setStartDate(null);

        // Create the FleetTrip, which fails.

        restFleetTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTrip)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFleetTrips() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList
        restFleetTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fleetTrip.getId().intValue())))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].driverId").value(hasItem(DEFAULT_DRIVER_ID.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].distanceKm").value(hasItem(DEFAULT_DISTANCE_KM)))
            .andExpect(jsonPath("$.[*].startLocation").value(hasItem(DEFAULT_START_LOCATION)))
            .andExpect(jsonPath("$.[*].endLocation").value(hasItem(DEFAULT_END_LOCATION)))
            .andExpect(jsonPath("$.[*].loadType").value(hasItem(DEFAULT_LOAD_TYPE)))
            .andExpect(jsonPath("$.[*].loadDescription").value(hasItem(DEFAULT_LOAD_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].routeGeoCoordinates").value(hasItem(DEFAULT_ROUTE_GEO_COORDINATES)))
            .andExpect(jsonPath("$.[*].tripCost").value(hasItem(sameNumber(DEFAULT_TRIP_COST))));
    }

    @Test
    @Transactional
    void getFleetTrip() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get the fleetTrip
        restFleetTripMockMvc
            .perform(get(ENTITY_API_URL_ID, fleetTrip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fleetTrip.getId().intValue()))
            .andExpect(jsonPath("$.vehicleId").value(DEFAULT_VEHICLE_ID.intValue()))
            .andExpect(jsonPath("$.driverId").value(DEFAULT_DRIVER_ID.intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.distanceKm").value(DEFAULT_DISTANCE_KM))
            .andExpect(jsonPath("$.startLocation").value(DEFAULT_START_LOCATION))
            .andExpect(jsonPath("$.endLocation").value(DEFAULT_END_LOCATION))
            .andExpect(jsonPath("$.loadType").value(DEFAULT_LOAD_TYPE))
            .andExpect(jsonPath("$.loadDescription").value(DEFAULT_LOAD_DESCRIPTION))
            .andExpect(jsonPath("$.routeGeoCoordinates").value(DEFAULT_ROUTE_GEO_COORDINATES))
            .andExpect(jsonPath("$.tripCost").value(sameNumber(DEFAULT_TRIP_COST)));
    }

    @Test
    @Transactional
    void getFleetTripsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        Long id = fleetTrip.getId();

        defaultFleetTripFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFleetTripFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFleetTripFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFleetTripsByVehicleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where vehicleId equals to
        defaultFleetTripFiltering("vehicleId.equals=" + DEFAULT_VEHICLE_ID, "vehicleId.equals=" + UPDATED_VEHICLE_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByVehicleIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where vehicleId in
        defaultFleetTripFiltering("vehicleId.in=" + DEFAULT_VEHICLE_ID + "," + UPDATED_VEHICLE_ID, "vehicleId.in=" + UPDATED_VEHICLE_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByVehicleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where vehicleId is not null
        defaultFleetTripFiltering("vehicleId.specified=true", "vehicleId.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetTripsByVehicleIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where vehicleId is greater than or equal to
        defaultFleetTripFiltering(
            "vehicleId.greaterThanOrEqual=" + DEFAULT_VEHICLE_ID,
            "vehicleId.greaterThanOrEqual=" + UPDATED_VEHICLE_ID
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByVehicleIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where vehicleId is less than or equal to
        defaultFleetTripFiltering("vehicleId.lessThanOrEqual=" + DEFAULT_VEHICLE_ID, "vehicleId.lessThanOrEqual=" + SMALLER_VEHICLE_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByVehicleIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where vehicleId is less than
        defaultFleetTripFiltering("vehicleId.lessThan=" + UPDATED_VEHICLE_ID, "vehicleId.lessThan=" + DEFAULT_VEHICLE_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByVehicleIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where vehicleId is greater than
        defaultFleetTripFiltering("vehicleId.greaterThan=" + SMALLER_VEHICLE_ID, "vehicleId.greaterThan=" + DEFAULT_VEHICLE_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByDriverIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where driverId equals to
        defaultFleetTripFiltering("driverId.equals=" + DEFAULT_DRIVER_ID, "driverId.equals=" + UPDATED_DRIVER_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByDriverIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where driverId in
        defaultFleetTripFiltering("driverId.in=" + DEFAULT_DRIVER_ID + "," + UPDATED_DRIVER_ID, "driverId.in=" + UPDATED_DRIVER_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByDriverIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where driverId is not null
        defaultFleetTripFiltering("driverId.specified=true", "driverId.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetTripsByDriverIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where driverId is greater than or equal to
        defaultFleetTripFiltering("driverId.greaterThanOrEqual=" + DEFAULT_DRIVER_ID, "driverId.greaterThanOrEqual=" + UPDATED_DRIVER_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByDriverIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where driverId is less than or equal to
        defaultFleetTripFiltering("driverId.lessThanOrEqual=" + DEFAULT_DRIVER_ID, "driverId.lessThanOrEqual=" + SMALLER_DRIVER_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByDriverIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where driverId is less than
        defaultFleetTripFiltering("driverId.lessThan=" + UPDATED_DRIVER_ID, "driverId.lessThan=" + DEFAULT_DRIVER_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByDriverIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where driverId is greater than
        defaultFleetTripFiltering("driverId.greaterThan=" + SMALLER_DRIVER_ID, "driverId.greaterThan=" + DEFAULT_DRIVER_ID);
    }

    @Test
    @Transactional
    void getAllFleetTripsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where startDate equals to
        defaultFleetTripFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFleetTripsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where startDate in
        defaultFleetTripFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllFleetTripsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where startDate is not null
        defaultFleetTripFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetTripsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where endDate equals to
        defaultFleetTripFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFleetTripsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where endDate in
        defaultFleetTripFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllFleetTripsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where endDate is not null
        defaultFleetTripFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetTripsByDistanceKmIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where distanceKm equals to
        defaultFleetTripFiltering("distanceKm.equals=" + DEFAULT_DISTANCE_KM, "distanceKm.equals=" + UPDATED_DISTANCE_KM);
    }

    @Test
    @Transactional
    void getAllFleetTripsByDistanceKmIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where distanceKm in
        defaultFleetTripFiltering(
            "distanceKm.in=" + DEFAULT_DISTANCE_KM + "," + UPDATED_DISTANCE_KM,
            "distanceKm.in=" + UPDATED_DISTANCE_KM
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByDistanceKmIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where distanceKm is not null
        defaultFleetTripFiltering("distanceKm.specified=true", "distanceKm.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetTripsByDistanceKmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where distanceKm is greater than or equal to
        defaultFleetTripFiltering(
            "distanceKm.greaterThanOrEqual=" + DEFAULT_DISTANCE_KM,
            "distanceKm.greaterThanOrEqual=" + UPDATED_DISTANCE_KM
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByDistanceKmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where distanceKm is less than or equal to
        defaultFleetTripFiltering("distanceKm.lessThanOrEqual=" + DEFAULT_DISTANCE_KM, "distanceKm.lessThanOrEqual=" + SMALLER_DISTANCE_KM);
    }

    @Test
    @Transactional
    void getAllFleetTripsByDistanceKmIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where distanceKm is less than
        defaultFleetTripFiltering("distanceKm.lessThan=" + UPDATED_DISTANCE_KM, "distanceKm.lessThan=" + DEFAULT_DISTANCE_KM);
    }

    @Test
    @Transactional
    void getAllFleetTripsByDistanceKmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where distanceKm is greater than
        defaultFleetTripFiltering("distanceKm.greaterThan=" + SMALLER_DISTANCE_KM, "distanceKm.greaterThan=" + DEFAULT_DISTANCE_KM);
    }

    @Test
    @Transactional
    void getAllFleetTripsByStartLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where startLocation equals to
        defaultFleetTripFiltering("startLocation.equals=" + DEFAULT_START_LOCATION, "startLocation.equals=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllFleetTripsByStartLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where startLocation in
        defaultFleetTripFiltering(
            "startLocation.in=" + DEFAULT_START_LOCATION + "," + UPDATED_START_LOCATION,
            "startLocation.in=" + UPDATED_START_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByStartLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where startLocation is not null
        defaultFleetTripFiltering("startLocation.specified=true", "startLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetTripsByStartLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where startLocation contains
        defaultFleetTripFiltering("startLocation.contains=" + DEFAULT_START_LOCATION, "startLocation.contains=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllFleetTripsByStartLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where startLocation does not contain
        defaultFleetTripFiltering(
            "startLocation.doesNotContain=" + UPDATED_START_LOCATION,
            "startLocation.doesNotContain=" + DEFAULT_START_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByEndLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where endLocation equals to
        defaultFleetTripFiltering("endLocation.equals=" + DEFAULT_END_LOCATION, "endLocation.equals=" + UPDATED_END_LOCATION);
    }

    @Test
    @Transactional
    void getAllFleetTripsByEndLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where endLocation in
        defaultFleetTripFiltering(
            "endLocation.in=" + DEFAULT_END_LOCATION + "," + UPDATED_END_LOCATION,
            "endLocation.in=" + UPDATED_END_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByEndLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where endLocation is not null
        defaultFleetTripFiltering("endLocation.specified=true", "endLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetTripsByEndLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where endLocation contains
        defaultFleetTripFiltering("endLocation.contains=" + DEFAULT_END_LOCATION, "endLocation.contains=" + UPDATED_END_LOCATION);
    }

    @Test
    @Transactional
    void getAllFleetTripsByEndLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where endLocation does not contain
        defaultFleetTripFiltering(
            "endLocation.doesNotContain=" + UPDATED_END_LOCATION,
            "endLocation.doesNotContain=" + DEFAULT_END_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByLoadTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where loadType equals to
        defaultFleetTripFiltering("loadType.equals=" + DEFAULT_LOAD_TYPE, "loadType.equals=" + UPDATED_LOAD_TYPE);
    }

    @Test
    @Transactional
    void getAllFleetTripsByLoadTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where loadType in
        defaultFleetTripFiltering("loadType.in=" + DEFAULT_LOAD_TYPE + "," + UPDATED_LOAD_TYPE, "loadType.in=" + UPDATED_LOAD_TYPE);
    }

    @Test
    @Transactional
    void getAllFleetTripsByLoadTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where loadType is not null
        defaultFleetTripFiltering("loadType.specified=true", "loadType.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetTripsByLoadTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where loadType contains
        defaultFleetTripFiltering("loadType.contains=" + DEFAULT_LOAD_TYPE, "loadType.contains=" + UPDATED_LOAD_TYPE);
    }

    @Test
    @Transactional
    void getAllFleetTripsByLoadTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where loadType does not contain
        defaultFleetTripFiltering("loadType.doesNotContain=" + UPDATED_LOAD_TYPE, "loadType.doesNotContain=" + DEFAULT_LOAD_TYPE);
    }

    @Test
    @Transactional
    void getAllFleetTripsByLoadDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where loadDescription equals to
        defaultFleetTripFiltering(
            "loadDescription.equals=" + DEFAULT_LOAD_DESCRIPTION,
            "loadDescription.equals=" + UPDATED_LOAD_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByLoadDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where loadDescription in
        defaultFleetTripFiltering(
            "loadDescription.in=" + DEFAULT_LOAD_DESCRIPTION + "," + UPDATED_LOAD_DESCRIPTION,
            "loadDescription.in=" + UPDATED_LOAD_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByLoadDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where loadDescription is not null
        defaultFleetTripFiltering("loadDescription.specified=true", "loadDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetTripsByLoadDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where loadDescription contains
        defaultFleetTripFiltering(
            "loadDescription.contains=" + DEFAULT_LOAD_DESCRIPTION,
            "loadDescription.contains=" + UPDATED_LOAD_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByLoadDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where loadDescription does not contain
        defaultFleetTripFiltering(
            "loadDescription.doesNotContain=" + UPDATED_LOAD_DESCRIPTION,
            "loadDescription.doesNotContain=" + DEFAULT_LOAD_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFleetTripsByTripCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where tripCost equals to
        defaultFleetTripFiltering("tripCost.equals=" + DEFAULT_TRIP_COST, "tripCost.equals=" + UPDATED_TRIP_COST);
    }

    @Test
    @Transactional
    void getAllFleetTripsByTripCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where tripCost in
        defaultFleetTripFiltering("tripCost.in=" + DEFAULT_TRIP_COST + "," + UPDATED_TRIP_COST, "tripCost.in=" + UPDATED_TRIP_COST);
    }

    @Test
    @Transactional
    void getAllFleetTripsByTripCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where tripCost is not null
        defaultFleetTripFiltering("tripCost.specified=true", "tripCost.specified=false");
    }

    @Test
    @Transactional
    void getAllFleetTripsByTripCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where tripCost is greater than or equal to
        defaultFleetTripFiltering("tripCost.greaterThanOrEqual=" + DEFAULT_TRIP_COST, "tripCost.greaterThanOrEqual=" + UPDATED_TRIP_COST);
    }

    @Test
    @Transactional
    void getAllFleetTripsByTripCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where tripCost is less than or equal to
        defaultFleetTripFiltering("tripCost.lessThanOrEqual=" + DEFAULT_TRIP_COST, "tripCost.lessThanOrEqual=" + SMALLER_TRIP_COST);
    }

    @Test
    @Transactional
    void getAllFleetTripsByTripCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where tripCost is less than
        defaultFleetTripFiltering("tripCost.lessThan=" + UPDATED_TRIP_COST, "tripCost.lessThan=" + DEFAULT_TRIP_COST);
    }

    @Test
    @Transactional
    void getAllFleetTripsByTripCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        // Get all the fleetTripList where tripCost is greater than
        defaultFleetTripFiltering("tripCost.greaterThan=" + SMALLER_TRIP_COST, "tripCost.greaterThan=" + DEFAULT_TRIP_COST);
    }

    private void defaultFleetTripFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFleetTripShouldBeFound(shouldBeFound);
        defaultFleetTripShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFleetTripShouldBeFound(String filter) throws Exception {
        restFleetTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fleetTrip.getId().intValue())))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].driverId").value(hasItem(DEFAULT_DRIVER_ID.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].distanceKm").value(hasItem(DEFAULT_DISTANCE_KM)))
            .andExpect(jsonPath("$.[*].startLocation").value(hasItem(DEFAULT_START_LOCATION)))
            .andExpect(jsonPath("$.[*].endLocation").value(hasItem(DEFAULT_END_LOCATION)))
            .andExpect(jsonPath("$.[*].loadType").value(hasItem(DEFAULT_LOAD_TYPE)))
            .andExpect(jsonPath("$.[*].loadDescription").value(hasItem(DEFAULT_LOAD_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].routeGeoCoordinates").value(hasItem(DEFAULT_ROUTE_GEO_COORDINATES)))
            .andExpect(jsonPath("$.[*].tripCost").value(hasItem(sameNumber(DEFAULT_TRIP_COST))));

        // Check, that the count call also returns 1
        restFleetTripMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFleetTripShouldNotBeFound(String filter) throws Exception {
        restFleetTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFleetTripMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFleetTrip() throws Exception {
        // Get the fleetTrip
        restFleetTripMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFleetTrip() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fleetTrip
        FleetTrip updatedFleetTrip = fleetTripRepository.findById(fleetTrip.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFleetTrip are not directly saved in db
        em.detach(updatedFleetTrip);
        updatedFleetTrip
            .vehicleId(UPDATED_VEHICLE_ID)
            .driverId(UPDATED_DRIVER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .distanceKm(UPDATED_DISTANCE_KM)
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .loadType(UPDATED_LOAD_TYPE)
            .loadDescription(UPDATED_LOAD_DESCRIPTION)
            .routeGeoCoordinates(UPDATED_ROUTE_GEO_COORDINATES)
            .tripCost(UPDATED_TRIP_COST);

        restFleetTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFleetTrip.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFleetTrip))
            )
            .andExpect(status().isOk());

        // Validate the FleetTrip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFleetTripToMatchAllProperties(updatedFleetTrip);
    }

    @Test
    @Transactional
    void putNonExistingFleetTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTrip.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFleetTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fleetTrip.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTrip))
            )
            .andExpect(status().isBadRequest());

        // Validate the FleetTrip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFleetTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTrip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fleetTrip))
            )
            .andExpect(status().isBadRequest());

        // Validate the FleetTrip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFleetTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTrip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetTripMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fleetTrip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FleetTrip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFleetTripWithPatch() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fleetTrip using partial update
        FleetTrip partialUpdatedFleetTrip = new FleetTrip();
        partialUpdatedFleetTrip.setId(fleetTrip.getId());

        partialUpdatedFleetTrip
            .vehicleId(UPDATED_VEHICLE_ID)
            .driverId(UPDATED_DRIVER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .routeGeoCoordinates(UPDATED_ROUTE_GEO_COORDINATES);

        restFleetTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFleetTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFleetTrip))
            )
            .andExpect(status().isOk());

        // Validate the FleetTrip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFleetTripUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFleetTrip, fleetTrip),
            getPersistedFleetTrip(fleetTrip)
        );
    }

    @Test
    @Transactional
    void fullUpdateFleetTripWithPatch() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fleetTrip using partial update
        FleetTrip partialUpdatedFleetTrip = new FleetTrip();
        partialUpdatedFleetTrip.setId(fleetTrip.getId());

        partialUpdatedFleetTrip
            .vehicleId(UPDATED_VEHICLE_ID)
            .driverId(UPDATED_DRIVER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .distanceKm(UPDATED_DISTANCE_KM)
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .loadType(UPDATED_LOAD_TYPE)
            .loadDescription(UPDATED_LOAD_DESCRIPTION)
            .routeGeoCoordinates(UPDATED_ROUTE_GEO_COORDINATES)
            .tripCost(UPDATED_TRIP_COST);

        restFleetTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFleetTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFleetTrip))
            )
            .andExpect(status().isOk());

        // Validate the FleetTrip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFleetTripUpdatableFieldsEquals(partialUpdatedFleetTrip, getPersistedFleetTrip(partialUpdatedFleetTrip));
    }

    @Test
    @Transactional
    void patchNonExistingFleetTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTrip.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFleetTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fleetTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fleetTrip))
            )
            .andExpect(status().isBadRequest());

        // Validate the FleetTrip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFleetTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTrip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fleetTrip))
            )
            .andExpect(status().isBadRequest());

        // Validate the FleetTrip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFleetTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fleetTrip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFleetTripMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fleetTrip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FleetTrip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFleetTrip() throws Exception {
        // Initialize the database
        insertedFleetTrip = fleetTripRepository.saveAndFlush(fleetTrip);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fleetTrip
        restFleetTripMockMvc
            .perform(delete(ENTITY_API_URL_ID, fleetTrip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fleetTripRepository.count();
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

    protected FleetTrip getPersistedFleetTrip(FleetTrip fleetTrip) {
        return fleetTripRepository.findById(fleetTrip.getId()).orElseThrow();
    }

    protected void assertPersistedFleetTripToMatchAllProperties(FleetTrip expectedFleetTrip) {
        assertFleetTripAllPropertiesEquals(expectedFleetTrip, getPersistedFleetTrip(expectedFleetTrip));
    }

    protected void assertPersistedFleetTripToMatchUpdatableProperties(FleetTrip expectedFleetTrip) {
        assertFleetTripAllUpdatablePropertiesEquals(expectedFleetTrip, getPersistedFleetTrip(expectedFleetTrip));
    }
}
