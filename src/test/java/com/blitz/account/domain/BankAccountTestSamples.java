package com.blitz.account.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BankAccountTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BankAccount getBankAccountSample1() {
        return new BankAccount().id(1L).name("name1").accountNumber("accountNumber1").bankName("bankName1");
    }

    public static BankAccount getBankAccountSample2() {
        return new BankAccount().id(2L).name("name2").accountNumber("accountNumber2").bankName("bankName2");
    }

    public static BankAccount getBankAccountRandomSampleGenerator() {
        return new BankAccount()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .accountNumber(UUID.randomUUID().toString())
            .bankName(UUID.randomUUID().toString());
    }
}
