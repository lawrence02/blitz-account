package com.blitz.account.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class JournalLineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static JournalLine getJournalLineSample1() {
        return new JournalLine().id(1L).journalId(1L).accountId(1L);
    }

    public static JournalLine getJournalLineSample2() {
        return new JournalLine().id(2L).journalId(2L).accountId(2L);
    }

    public static JournalLine getJournalLineRandomSampleGenerator() {
        return new JournalLine()
            .id(longCount.incrementAndGet())
            .journalId(longCount.incrementAndGet())
            .accountId(longCount.incrementAndGet());
    }
}
