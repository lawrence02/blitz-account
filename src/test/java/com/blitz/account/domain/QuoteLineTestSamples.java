package com.blitz.account.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuoteLineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QuoteLine getQuoteLineSample1() {
        return new QuoteLine().id(1L).quoteId(1L).productId(1L).quantity(1).vatRateId(1L);
    }

    public static QuoteLine getQuoteLineSample2() {
        return new QuoteLine().id(2L).quoteId(2L).productId(2L).quantity(2).vatRateId(2L);
    }

    public static QuoteLine getQuoteLineRandomSampleGenerator() {
        return new QuoteLine()
            .id(longCount.incrementAndGet())
            .quoteId(longCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .vatRateId(longCount.incrementAndGet());
    }
}
