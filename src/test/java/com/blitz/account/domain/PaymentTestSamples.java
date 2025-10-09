package com.blitz.account.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Payment getPaymentSample1() {
        return new Payment().id(1L).currencyId(1L).bankAccountId(1L).invoiceId(1L);
    }

    public static Payment getPaymentSample2() {
        return new Payment().id(2L).currencyId(2L).bankAccountId(2L).invoiceId(2L);
    }

    public static Payment getPaymentRandomSampleGenerator() {
        return new Payment()
            .id(longCount.incrementAndGet())
            .currencyId(longCount.incrementAndGet())
            .bankAccountId(longCount.incrementAndGet())
            .invoiceId(longCount.incrementAndGet());
    }
}
