package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ServiceLog getServiceLogSample1() {
        return new ServiceLog().id(1L).vehicleId(1L).description("description1").supplierId(1L);
    }

    public static ServiceLog getServiceLogSample2() {
        return new ServiceLog().id(2L).vehicleId(2L).description("description2").supplierId(2L);
    }

    public static ServiceLog getServiceLogRandomSampleGenerator() {
        return new ServiceLog()
            .id(longCount.incrementAndGet())
            .vehicleId(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .supplierId(longCount.incrementAndGet());
    }
}
