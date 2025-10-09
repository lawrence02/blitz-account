package com.blitz.account.domain;

import static com.blitz.account.domain.ServiceLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceLog.class);
        ServiceLog serviceLog1 = getServiceLogSample1();
        ServiceLog serviceLog2 = new ServiceLog();
        assertThat(serviceLog1).isNotEqualTo(serviceLog2);

        serviceLog2.setId(serviceLog1.getId());
        assertThat(serviceLog1).isEqualTo(serviceLog2);

        serviceLog2 = getServiceLogSample2();
        assertThat(serviceLog1).isNotEqualTo(serviceLog2);
    }
}
