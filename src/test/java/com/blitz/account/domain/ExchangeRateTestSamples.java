package com.blitz.account.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ExchangeRateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExchangeRate getExchangeRateSample1() {
        return new ExchangeRate().id(1L).baseCurrencyId(1L).targetCurrencyId(1L);
    }

    public static ExchangeRate getExchangeRateSample2() {
        return new ExchangeRate().id(2L).baseCurrencyId(2L).targetCurrencyId(2L);
    }

    public static ExchangeRate getExchangeRateRandomSampleGenerator() {
        return new ExchangeRate()
            .id(longCount.incrementAndGet())
            .baseCurrencyId(longCount.incrementAndGet())
            .targetCurrencyId(longCount.incrementAndGet());
    }
}
