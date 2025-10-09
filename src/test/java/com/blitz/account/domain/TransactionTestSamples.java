package com.blitz.account.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transaction getTransactionSample1() {
        return new Transaction().id(1L).accountId(1L).vatRateId(1L).currencyId(1L);
    }

    public static Transaction getTransactionSample2() {
        return new Transaction().id(2L).accountId(2L).vatRateId(2L).currencyId(2L);
    }

    public static Transaction getTransactionRandomSampleGenerator() {
        return new Transaction()
            .id(longCount.incrementAndGet())
            .accountId(longCount.incrementAndGet())
            .vatRateId(longCount.incrementAndGet())
            .currencyId(longCount.incrementAndGet());
    }
}
