package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Quote getQuoteSample1() {
        return new Quote().id(1L).clientName("clientName1").currencyId(1L).vatRateId(1L);
    }

    public static Quote getQuoteSample2() {
        return new Quote().id(2L).clientName("clientName2").currencyId(2L).vatRateId(2L);
    }

    public static Quote getQuoteRandomSampleGenerator() {
        return new Quote()
            .id(longCount.incrementAndGet())
            .clientName(UUID.randomUUID().toString())
            .currencyId(longCount.incrementAndGet())
            .vatRateId(longCount.incrementAndGet());
    }
}
