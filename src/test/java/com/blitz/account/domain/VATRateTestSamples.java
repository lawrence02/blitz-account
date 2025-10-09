package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VATRateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VATRate getVATRateSample1() {
        return new VATRate().id(1L).name("name1");
    }

    public static VATRate getVATRateSample2() {
        return new VATRate().id(2L).name("name2");
    }

    public static VATRate getVATRateRandomSampleGenerator() {
        return new VATRate().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
