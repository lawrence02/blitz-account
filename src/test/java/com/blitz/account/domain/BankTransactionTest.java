package com.blitz.account.domain;

import static com.blitz.account.domain.BankTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankTransaction.class);
        BankTransaction bankTransaction1 = getBankTransactionSample1();
        BankTransaction bankTransaction2 = new BankTransaction();
        assertThat(bankTransaction1).isNotEqualTo(bankTransaction2);

        bankTransaction2.setId(bankTransaction1.getId());
        assertThat(bankTransaction1).isEqualTo(bankTransaction2);

        bankTransaction2 = getBankTransactionSample2();
        assertThat(bankTransaction1).isNotEqualTo(bankTransaction2);
    }
}
