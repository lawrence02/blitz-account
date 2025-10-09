package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecurringTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RecurringTransaction getRecurringTransactionSample1() {
        return new RecurringTransaction().id(1L).name("name1").frequency("frequency1").accountId(1L).currencyId(1L).vatRateId(1L);
    }

    public static RecurringTransaction getRecurringTransactionSample2() {
        return new RecurringTransaction().id(2L).name("name2").frequency("frequency2").accountId(2L).currencyId(2L).vatRateId(2L);
    }

    public static RecurringTransaction getRecurringTransactionRandomSampleGenerator() {
        return new RecurringTransaction()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .frequency(UUID.randomUUID().toString())
            .accountId(longCount.incrementAndGet())
            .currencyId(longCount.incrementAndGet())
            .vatRateId(longCount.incrementAndGet());
    }
}
