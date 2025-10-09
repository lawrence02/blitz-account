package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FleetTripTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FleetTrip getFleetTripSample1() {
        return new FleetTrip()
            .id(1L)
            .vehicleId(1L)
            .driverId(1L)
            .startLocation("startLocation1")
            .endLocation("endLocation1")
            .loadType("loadType1")
            .loadDescription("loadDescription1");
    }

    public static FleetTrip getFleetTripSample2() {
        return new FleetTrip()
            .id(2L)
            .vehicleId(2L)
            .driverId(2L)
            .startLocation("startLocation2")
            .endLocation("endLocation2")
            .loadType("loadType2")
            .loadDescription("loadDescription2");
    }

    public static FleetTrip getFleetTripRandomSampleGenerator() {
        return new FleetTrip()
            .id(longCount.incrementAndGet())
            .vehicleId(longCount.incrementAndGet())
            .driverId(longCount.incrementAndGet())
            .startLocation(UUID.randomUUID().toString())
            .endLocation(UUID.randomUUID().toString())
            .loadType(UUID.randomUUID().toString())
            .loadDescription(UUID.randomUUID().toString());
    }
}
