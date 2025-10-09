package com.blitz.account.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A JournalLine.
 */
@Entity
@Table(name = "journal_line")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JournalLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "journal_id", nullable = false)
    private Long journalId;

    @NotNull
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "debit", precision = 21, scale = 2)
    private BigDecimal debit;

    @Column(name = "credit", precision = 21, scale = 2)
    private BigDecimal credit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JournalLine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJournalId() {
        return this.journalId;
    }

    public JournalLine journalId(Long journalId) {
        this.setJournalId(journalId);
        return this;
    }

    public void setJournalId(Long journalId) {
        this.journalId = journalId;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public JournalLine accountId(Long accountId) {
        this.setAccountId(accountId);
        return this;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getDebit() {
        return this.debit;
    }

    public JournalLine debit(BigDecimal debit) {
        this.setDebit(debit);
        return this;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return this.credit;
    }

    public JournalLine credit(BigDecimal credit) {
        this.setCredit(credit);
        return this;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JournalLine)) {
            return false;
        }
        return getId() != null && getId().equals(((JournalLine) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JournalLine{" +
            "id=" + getId() +
            ", journalId=" + getJournalId() +
            ", accountId=" + getAccountId() +
            ", debit=" + getDebit() +
            ", credit=" + getCredit() +
            "}";
    }
}
