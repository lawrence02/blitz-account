package com.blitz.account.domain;

import static com.blitz.account.domain.IncidentLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncidentLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncidentLog.class);
        IncidentLog incidentLog1 = getIncidentLogSample1();
        IncidentLog incidentLog2 = new IncidentLog();
        assertThat(incidentLog1).isNotEqualTo(incidentLog2);

        incidentLog2.setId(incidentLog1.getId());
        assertThat(incidentLog1).isEqualTo(incidentLog2);

        incidentLog2 = getIncidentLogSample2();
        assertThat(incidentLog1).isNotEqualTo(incidentLog2);
    }
}
