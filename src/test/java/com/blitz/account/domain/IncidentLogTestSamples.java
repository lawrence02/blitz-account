package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IncidentLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IncidentLog getIncidentLogSample1() {
        return new IncidentLog().id(1L).vehicleId(1L).tripId(1L).description("description1");
    }

    public static IncidentLog getIncidentLogSample2() {
        return new IncidentLog().id(2L).vehicleId(2L).tripId(2L).description("description2");
    }

    public static IncidentLog getIncidentLogRandomSampleGenerator() {
        return new IncidentLog()
            .id(longCount.incrementAndGet())
            .vehicleId(longCount.incrementAndGet())
            .tripId(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
