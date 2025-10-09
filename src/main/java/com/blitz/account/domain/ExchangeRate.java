package com.blitz.account.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExchangeRate.
 */
@Entity
@Table(name = "exchange_rate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExchangeRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "base_currency_id", nullable = false)
    private Long baseCurrencyId;

    @NotNull
    @Column(name = "target_currency_id", nullable = false)
    private Long targetCurrencyId;

    @NotNull
    @Column(name = "rate", precision = 21, scale = 2, nullable = false)
    private BigDecimal rate;

    @NotNull
    @Column(name = "rate_date", nullable = false)
    private Instant rateDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExchangeRate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBaseCurrencyId() {
        return this.baseCurrencyId;
    }

    public ExchangeRate baseCurrencyId(Long baseCurrencyId) {
        this.setBaseCurrencyId(baseCurrencyId);
        return this;
    }

    public void setBaseCurrencyId(Long baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public Long getTargetCurrencyId() {
        return this.targetCurrencyId;
    }

    public ExchangeRate targetCurrencyId(Long targetCurrencyId) {
        this.setTargetCurrencyId(targetCurrencyId);
        return this;
    }

    public void setTargetCurrencyId(Long targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return this.rate;
    }

    public ExchangeRate rate(BigDecimal rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Instant getRateDate() {
        return this.rateDate;
    }

    public ExchangeRate rateDate(Instant rateDate) {
        this.setRateDate(rateDate);
        return this;
    }

    public void setRateDate(Instant rateDate) {
        this.rateDate = rateDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExchangeRate)) {
            return false;
        }
        return getId() != null && getId().equals(((ExchangeRate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExchangeRate{" +
            "id=" + getId() +
            ", baseCurrencyId=" + getBaseCurrencyId() +
            ", targetCurrencyId=" + getTargetCurrencyId() +
            ", rate=" + getRate() +
            ", rateDate='" + getRateDate() + "'" +
            "}";
    }
}
