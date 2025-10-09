package com.blitz.account.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceLineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InvoiceLine getInvoiceLineSample1() {
        return new InvoiceLine().id(1L).invoiceId(1L).productId(1L).quantity(1).vatRateId(1L);
    }

    public static InvoiceLine getInvoiceLineSample2() {
        return new InvoiceLine().id(2L).invoiceId(2L).productId(2L).quantity(2).vatRateId(2L);
    }

    public static InvoiceLine getInvoiceLineRandomSampleGenerator() {
        return new InvoiceLine()
            .id(longCount.incrementAndGet())
            .invoiceId(longCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .vatRateId(longCount.incrementAndGet());
    }
}
