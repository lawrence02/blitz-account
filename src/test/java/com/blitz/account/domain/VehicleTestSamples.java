package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vehicle getVehicleSample1() {
        return new Vehicle().id(1L).name("name1").licensePlate("licensePlate1").type("type1");
    }

    public static Vehicle getVehicleSample2() {
        return new Vehicle().id(2L).name("name2").licensePlate("licensePlate2").type("type2");
    }

    public static Vehicle getVehicleRandomSampleGenerator() {
        return new Vehicle()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .licensePlate(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString());
    }
}
