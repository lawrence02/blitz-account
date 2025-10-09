package com.blitz.account.domain;

import static com.blitz.account.domain.FleetTripTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FleetTripTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FleetTrip.class);
        FleetTrip fleetTrip1 = getFleetTripSample1();
        FleetTrip fleetTrip2 = new FleetTrip();
        assertThat(fleetTrip1).isNotEqualTo(fleetTrip2);

        fleetTrip2.setId(fleetTrip1.getId());
        assertThat(fleetTrip1).isEqualTo(fleetTrip2);

        fleetTrip2 = getFleetTripSample2();
        assertThat(fleetTrip1).isNotEqualTo(fleetTrip2);
    }
}
