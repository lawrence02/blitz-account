package com.blitz.account.domain;

import static com.blitz.account.domain.JournalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JournalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Journal.class);
        Journal journal1 = getJournalSample1();
        Journal journal2 = new Journal();
        assertThat(journal1).isNotEqualTo(journal2);

        journal2.setId(journal1.getId());
        assertThat(journal1).isEqualTo(journal2);

        journal2 = getJournalSample2();
        assertThat(journal1).isNotEqualTo(journal2);
    }
}
