package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChartOfAccountTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChartOfAccount getChartOfAccountSample1() {
        return new ChartOfAccount().id(1L).name("name1").code("code1");
    }

    public static ChartOfAccount getChartOfAccountSample2() {
        return new ChartOfAccount().id(2L).name("name2").code("code2");
    }

    public static ChartOfAccount getChartOfAccountRandomSampleGenerator() {
        return new ChartOfAccount().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).code(UUID.randomUUID().toString());
    }
}
