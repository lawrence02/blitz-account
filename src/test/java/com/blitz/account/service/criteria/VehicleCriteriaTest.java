package com.blitz.account.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VehicleCriteriaTest {

    @Test
    void newVehicleCriteriaHasAllFiltersNullTest() {
        var vehicleCriteria = new VehicleCriteria();
        assertThat(vehicleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void vehicleCriteriaFluentMethodsCreatesFiltersTest() {
        var vehicleCriteria = new VehicleCriteria();

        setAllFilters(vehicleCriteria);

        assertThat(vehicleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void vehicleCriteriaCopyCreatesNullFilterTest() {
        var vehicleCriteria = new VehicleCriteria();
        var copy = vehicleCriteria.copy();

        assertThat(vehicleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(vehicleCriteria)
        );
    }

    @Test
    void vehicleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var vehicleCriteria = new VehicleCriteria();
        setAllFilters(vehicleCriteria);

        var copy = vehicleCriteria.copy();

        assertThat(vehicleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(vehicleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var vehicleCriteria = new VehicleCriteria();

        assertThat(vehicleCriteria).hasToString("VehicleCriteria{}");
    }

    private static void setAllFilters(VehicleCriteria vehicleCriteria) {
        vehicleCriteria.id();
        vehicleCriteria.name();
        vehicleCriteria.licensePlate();
        vehicleCriteria.type();
        vehicleCriteria.currentMileage();
        vehicleCriteria.status();
        vehicleCriteria.distinct();
    }

    private static Condition<VehicleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getLicensePlate()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getCurrentMileage()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VehicleCriteria> copyFiltersAre(VehicleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getLicensePlate(), copy.getLicensePlate()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getCurrentMileage(), copy.getCurrentMileage()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
