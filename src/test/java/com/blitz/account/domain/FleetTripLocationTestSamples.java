package com.blitz.account.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FleetTripLocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FleetTripLocation getFleetTripLocationSample1() {
        return new FleetTripLocation().id(1L).fleetTripId(1L);
    }

    public static FleetTripLocation getFleetTripLocationSample2() {
        return new FleetTripLocation().id(2L).fleetTripId(2L);
    }

    public static FleetTripLocation getFleetTripLocationRandomSampleGenerator() {
        return new FleetTripLocation().id(longCount.incrementAndGet()).fleetTripId(longCount.incrementAndGet());
    }
}
