package com.blitz.account.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ChartOfAccountCriteriaTest {

    @Test
    void newChartOfAccountCriteriaHasAllFiltersNullTest() {
        var chartOfAccountCriteria = new ChartOfAccountCriteria();
        assertThat(chartOfAccountCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void chartOfAccountCriteriaFluentMethodsCreatesFiltersTest() {
        var chartOfAccountCriteria = new ChartOfAccountCriteria();

        setAllFilters(chartOfAccountCriteria);

        assertThat(chartOfAccountCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void chartOfAccountCriteriaCopyCreatesNullFilterTest() {
        var chartOfAccountCriteria = new ChartOfAccountCriteria();
        var copy = chartOfAccountCriteria.copy();

        assertThat(chartOfAccountCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(chartOfAccountCriteria)
        );
    }

    @Test
    void chartOfAccountCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var chartOfAccountCriteria = new ChartOfAccountCriteria();
        setAllFilters(chartOfAccountCriteria);

        var copy = chartOfAccountCriteria.copy();

        assertThat(chartOfAccountCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(chartOfAccountCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var chartOfAccountCriteria = new ChartOfAccountCriteria();

        assertThat(chartOfAccountCriteria).hasToString("ChartOfAccountCriteria{}");
    }

    private static void setAllFilters(ChartOfAccountCriteria chartOfAccountCriteria) {
        chartOfAccountCriteria.id();
        chartOfAccountCriteria.name();
        chartOfAccountCriteria.accountType();
        chartOfAccountCriteria.code();
        chartOfAccountCriteria.initialBalance();
        chartOfAccountCriteria.currentBalance();
        chartOfAccountCriteria.distinct();
    }

    private static Condition<ChartOfAccountCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAccountType()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getInitialBalance()) &&
                condition.apply(criteria.getCurrentBalance()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ChartOfAccountCriteria> copyFiltersAre(
        ChartOfAccountCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAccountType(), copy.getAccountType()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getInitialBalance(), copy.getInitialBalance()) &&
                condition.apply(criteria.getCurrentBalance(), copy.getCurrentBalance()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
