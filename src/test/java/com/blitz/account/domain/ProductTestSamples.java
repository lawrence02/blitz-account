package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product().id(1L).name("name1").categoryId(1L).supplierId(1L).stockQty(1);
    }

    public static Product getProductSample2() {
        return new Product().id(2L).name("name2").categoryId(2L).supplierId(2L).stockQty(2);
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .categoryId(longCount.incrementAndGet())
            .supplierId(longCount.incrementAndGet())
            .stockQty(intCount.incrementAndGet());
    }
}
