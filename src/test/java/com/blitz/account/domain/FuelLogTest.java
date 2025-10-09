package com.blitz.account.domain;

import static com.blitz.account.domain.FuelLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FuelLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FuelLog.class);
        FuelLog fuelLog1 = getFuelLogSample1();
        FuelLog fuelLog2 = new FuelLog();
        assertThat(fuelLog1).isNotEqualTo(fuelLog2);

        fuelLog2.setId(fuelLog1.getId());
        assertThat(fuelLog1).isEqualTo(fuelLog2);

        fuelLog2 = getFuelLogSample2();
        assertThat(fuelLog1).isNotEqualTo(fuelLog2);
    }
}
