package com.blitz.account.domain;

import static com.blitz.account.domain.ChartOfAccountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChartOfAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChartOfAccount.class);
        ChartOfAccount chartOfAccount1 = getChartOfAccountSample1();
        ChartOfAccount chartOfAccount2 = new ChartOfAccount();
        assertThat(chartOfAccount1).isNotEqualTo(chartOfAccount2);

        chartOfAccount2.setId(chartOfAccount1.getId());
        assertThat(chartOfAccount1).isEqualTo(chartOfAccount2);

        chartOfAccount2 = getChartOfAccountSample2();
        assertThat(chartOfAccount1).isNotEqualTo(chartOfAccount2);
    }
}
