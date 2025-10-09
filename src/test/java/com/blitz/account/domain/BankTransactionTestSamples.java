package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BankTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BankTransaction getBankTransactionSample1() {
        return new BankTransaction().id(1L).bankAccountId(1L).reference("reference1").relatedPaymentId(1L).description("description1");
    }

    public static BankTransaction getBankTransactionSample2() {
        return new BankTransaction().id(2L).bankAccountId(2L).reference("reference2").relatedPaymentId(2L).description("description2");
    }

    public static BankTransaction getBankTransactionRandomSampleGenerator() {
        return new BankTransaction()
            .id(longCount.incrementAndGet())
            .bankAccountId(longCount.incrementAndGet())
            .reference(UUID.randomUUID().toString())
            .relatedPaymentId(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
