package com.blitz.account.domain;

import com.blitz.account.domain.enumeration.TransactionDirection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BankTransaction.
 */
@Entity
@Table(name = "bank_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "bank_account_id", nullable = false)
    private Long bankAccountId;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private Instant transactionDate;

    @Column(name = "reference")
    private String reference;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private TransactionDirection direction;

    @Column(name = "related_payment_id")
    private Long relatedPaymentId;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BankTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBankAccountId() {
        return this.bankAccountId;
    }

    public BankTransaction bankAccountId(Long bankAccountId) {
        this.setBankAccountId(bankAccountId);
        return this;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public Instant getTransactionDate() {
        return this.transactionDate;
    }

    public BankTransaction transactionDate(Instant transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReference() {
        return this.reference;
    }

    public BankTransaction reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BankTransaction amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionDirection getDirection() {
        return this.direction;
    }

    public BankTransaction direction(TransactionDirection direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(TransactionDirection direction) {
        this.direction = direction;
    }

    public Long getRelatedPaymentId() {
        return this.relatedPaymentId;
    }

    public BankTransaction relatedPaymentId(Long relatedPaymentId) {
        this.setRelatedPaymentId(relatedPaymentId);
        return this;
    }

    public void setRelatedPaymentId(Long relatedPaymentId) {
        this.relatedPaymentId = relatedPaymentId;
    }

    public String getDescription() {
        return this.description;
    }

    public BankTransaction description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((BankTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankTransaction{" +
            "id=" + getId() +
            ", bankAccountId=" + getBankAccountId() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", reference='" + getReference() + "'" +
            ", amount=" + getAmount() +
            ", direction='" + getDirection() + "'" +
            ", relatedPaymentId=" + getRelatedPaymentId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
