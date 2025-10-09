package com.blitz.account.domain;

import static com.blitz.account.domain.RecurringTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecurringTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecurringTransaction.class);
        RecurringTransaction recurringTransaction1 = getRecurringTransactionSample1();
        RecurringTransaction recurringTransaction2 = new RecurringTransaction();
        assertThat(recurringTransaction1).isNotEqualTo(recurringTransaction2);

        recurringTransaction2.setId(recurringTransaction1.getId());
        assertThat(recurringTransaction1).isEqualTo(recurringTransaction2);

        recurringTransaction2 = getRecurringTransactionSample2();
        assertThat(recurringTransaction1).isNotEqualTo(recurringTransaction2);
    }
}
