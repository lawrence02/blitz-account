package com.blitz.account.domain;

import static com.blitz.account.domain.FleetTripLocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FleetTripLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FleetTripLocation.class);
        FleetTripLocation fleetTripLocation1 = getFleetTripLocationSample1();
        FleetTripLocation fleetTripLocation2 = new FleetTripLocation();
        assertThat(fleetTripLocation1).isNotEqualTo(fleetTripLocation2);

        fleetTripLocation2.setId(fleetTripLocation1.getId());
        assertThat(fleetTripLocation1).isEqualTo(fleetTripLocation2);

        fleetTripLocation2 = getFleetTripLocationSample2();
        assertThat(fleetTripLocation1).isNotEqualTo(fleetTripLocation2);
    }
}
