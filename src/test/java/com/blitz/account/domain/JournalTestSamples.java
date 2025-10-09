package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class JournalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Journal getJournalSample1() {
        return new Journal().id(1L).reference("reference1").description("description1");
    }

    public static Journal getJournalSample2() {
        return new Journal().id(2L).reference("reference2").description("description2");
    }

    public static Journal getJournalRandomSampleGenerator() {
        return new Journal()
            .id(longCount.incrementAndGet())
            .reference(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
