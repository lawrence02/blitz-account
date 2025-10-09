package com.blitz.account.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CashSaleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CashSale getCashSaleSample1() {
        return new CashSale().id(1L).productId(1L).quantity(1).vatRateId(1L).currencyId(1L);
    }

    public static CashSale getCashSaleSample2() {
        return new CashSale().id(2L).productId(2L).quantity(2).vatRateId(2L).currencyId(2L);
    }

    public static CashSale getCashSaleRandomSampleGenerator() {
        return new CashSale()
            .id(longCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .vatRateId(longCount.incrementAndGet())
            .currencyId(longCount.incrementAndGet());
    }
}
