package com.blitz.account.domain;

import static com.blitz.account.domain.JournalLineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JournalLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JournalLine.class);
        JournalLine journalLine1 = getJournalLineSample1();
        JournalLine journalLine2 = new JournalLine();
        assertThat(journalLine1).isNotEqualTo(journalLine2);

        journalLine2.setId(journalLine1.getId());
        assertThat(journalLine1).isEqualTo(journalLine2);

        journalLine2 = getJournalLineSample2();
        assertThat(journalLine1).isNotEqualTo(journalLine2);
    }
}
