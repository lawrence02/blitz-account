package com.blitz.account.domain;

import static com.blitz.account.domain.QuoteLineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuoteLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuoteLine.class);
        QuoteLine quoteLine1 = getQuoteLineSample1();
        QuoteLine quoteLine2 = new QuoteLine();
        assertThat(quoteLine1).isNotEqualTo(quoteLine2);

        quoteLine2.setId(quoteLine1.getId());
        assertThat(quoteLine1).isEqualTo(quoteLine2);

        quoteLine2 = getQuoteLineSample2();
        assertThat(quoteLine1).isNotEqualTo(quoteLine2);
    }
}
