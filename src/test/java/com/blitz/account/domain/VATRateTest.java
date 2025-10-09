package com.blitz.account.domain;

import static com.blitz.account.domain.VATRateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VATRateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VATRate.class);
        VATRate vATRate1 = getVATRateSample1();
        VATRate vATRate2 = new VATRate();
        assertThat(vATRate1).isNotEqualTo(vATRate2);

        vATRate2.setId(vATRate1.getId());
        assertThat(vATRate1).isEqualTo(vATRate2);

        vATRate2 = getVATRateSample2();
        assertThat(vATRate1).isNotEqualTo(vATRate2);
    }
}
