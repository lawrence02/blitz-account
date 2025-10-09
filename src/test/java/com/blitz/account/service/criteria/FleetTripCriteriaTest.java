package com.blitz.account.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FleetTripCriteriaTest {

    @Test
    void newFleetTripCriteriaHasAllFiltersNullTest() {
        var fleetTripCriteria = new FleetTripCriteria();
        assertThat(fleetTripCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void fleetTripCriteriaFluentMethodsCreatesFiltersTest() {
        var fleetTripCriteria = new FleetTripCriteria();

        setAllFilters(fleetTripCriteria);

        assertThat(fleetTripCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void fleetTripCriteriaCopyCreatesNullFilterTest() {
        var fleetTripCriteria = new FleetTripCriteria();
        var copy = fleetTripCriteria.copy();

        assertThat(fleetTripCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(fleetTripCriteria)
        );
    }

    @Test
    void fleetTripCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var fleetTripCriteria = new FleetTripCriteria();
        setAllFilters(fleetTripCriteria);

        var copy = fleetTripCriteria.copy();

        assertThat(fleetTripCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(fleetTripCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var fleetTripCriteria = new FleetTripCriteria();

        assertThat(fleetTripCriteria).hasToString("FleetTripCriteria{}");
    }

    private static void setAllFilters(FleetTripCriteria fleetTripCriteria) {
        fleetTripCriteria.id();
        fleetTripCriteria.vehicleId();
        fleetTripCriteria.driverId();
        fleetTripCriteria.startDate();
        fleetTripCriteria.endDate();
        fleetTripCriteria.distanceKm();
        fleetTripCriteria.startLocation();
        fleetTripCriteria.endLocation();
        fleetTripCriteria.loadType();
        fleetTripCriteria.loadDescription();
        fleetTripCriteria.tripCost();
        fleetTripCriteria.distinct();
    }

    private static Condition<FleetTripCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getVehicleId()) &&
                condition.apply(criteria.getDriverId()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getDistanceKm()) &&
                condition.apply(criteria.getStartLocation()) &&
                condition.apply(criteria.getEndLocation()) &&
                condition.apply(criteria.getLoadType()) &&
                condition.apply(criteria.getLoadDescription()) &&
                condition.apply(criteria.getTripCost()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FleetTripCriteria> copyFiltersAre(FleetTripCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getVehicleId(), copy.getVehicleId()) &&
                condition.apply(criteria.getDriverId(), copy.getDriverId()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getDistanceKm(), copy.getDistanceKm()) &&
                condition.apply(criteria.getStartLocation(), copy.getStartLocation()) &&
                condition.apply(criteria.getEndLocation(), copy.getEndLocation()) &&
                condition.apply(criteria.getLoadType(), copy.getLoadType()) &&
                condition.apply(criteria.getLoadDescription(), copy.getLoadDescription()) &&
                condition.apply(criteria.getTripCost(), copy.getTripCost()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
