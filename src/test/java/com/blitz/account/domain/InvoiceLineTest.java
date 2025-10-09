package com.blitz.account.domain;

import static com.blitz.account.domain.InvoiceLineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blitz.account.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceLine.class);
        InvoiceLine invoiceLine1 = getInvoiceLineSample1();
        InvoiceLine invoiceLine2 = new InvoiceLine();
        assertThat(invoiceLine1).isNotEqualTo(invoiceLine2);

        invoiceLine2.setId(invoiceLine1.getId());
        assertThat(invoiceLine1).isEqualTo(invoiceLine2);

        invoiceLine2 = getInvoiceLineSample2();
        assertThat(invoiceLine1).isNotEqualTo(invoiceLine2);
    }
}
