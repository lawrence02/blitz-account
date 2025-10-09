package com.blitz.account.domain;

import static com.blitz.account.domain.BudgetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BudgetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Budget.class);
        Budget budget1 = getBudgetSample1();
        Budget budget2 = new Budget();
        assertThat(budget1).isNotEqualTo(budget2);

        budget2.setId(budget1.getId());
        assertThat(budget1).isEqualTo(budget2);

        budget2 = getBudgetSample2();
        assertThat(budget1).isNotEqualTo(budget2);
    }
}
