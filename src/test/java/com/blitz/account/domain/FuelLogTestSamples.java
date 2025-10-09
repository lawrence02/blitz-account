package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FuelLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FuelLog getFuelLogSample1() {
        return new FuelLog().id(1L).vehicleId(1L).location("location1").tripId(1L);
    }

    public static FuelLog getFuelLogSample2() {
        return new FuelLog().id(2L).vehicleId(2L).location("location2").tripId(2L);
    }

    public static FuelLog getFuelLogRandomSampleGenerator() {
        return new FuelLog()
            .id(longCount.incrementAndGet())
            .vehicleId(longCount.incrementAndGet())
            .location(UUID.randomUUID().toString())
            .tripId(longCount.incrementAndGet());
    }
}
