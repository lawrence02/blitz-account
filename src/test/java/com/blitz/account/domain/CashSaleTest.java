package com.blitz.account.domain;

import static com.blitz.account.domain.CashSaleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CashSaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashSale.class);
        CashSale cashSale1 = getCashSaleSample1();
        CashSale cashSale2 = new CashSale();
        assertThat(cashSale1).isNotEqualTo(cashSale2);

        cashSale2.setId(cashSale1.getId());
        assertThat(cashSale1).isEqualTo(cashSale2);

        cashSale2 = getCashSaleSample2();
        assertThat(cashSale1).isNotEqualTo(cashSale2);
    }
}
