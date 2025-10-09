package com.blitz.account.domain;

import static com.blitz.account.domain.ExchangeRateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExchangeRateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExchangeRate.class);
        ExchangeRate exchangeRate1 = getExchangeRateSample1();
        ExchangeRate exchangeRate2 = new ExchangeRate();
        assertThat(exchangeRate1).isNotEqualTo(exchangeRate2);

        exchangeRate2.setId(exchangeRate1.getId());
        assertThat(exchangeRate1).isEqualTo(exchangeRate2);

        exchangeRate2 = getExchangeRateSample2();
        assertThat(exchangeRate1).isNotEqualTo(exchangeRate2);
    }
}
