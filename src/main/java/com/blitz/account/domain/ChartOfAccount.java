package com.blitz.account.domain;

import com.blitz.account.domain.enumeration.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ChartOfAccount.
 */
@Entity
@Table(name = "chart_of_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChartOfAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "initial_balance", precision = 21, scale = 2)
    private BigDecimal initialBalance;

    @Column(name = "current_balance", precision = 21, scale = 2)
    private BigDecimal currentBalance;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChartOfAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ChartOfAccount name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public ChartOfAccount accountType(AccountType accountType) {
        this.setAccountType(accountType);
        return this;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getCode() {
        return this.code;
    }

    public ChartOfAccount code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getInitialBalance() {
        return this.initialBalance;
    }

    public ChartOfAccount initialBalance(BigDecimal initialBalance) {
        this.setInitialBalance(initialBalance);
        return this;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getCurrentBalance() {
        return this.currentBalance;
    }

    public ChartOfAccount currentBalance(BigDecimal currentBalance) {
        this.setCurrentBalance(currentBalance);
        return this;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChartOfAccount)) {
            return false;
        }
        return getId() != null && getId().equals(((ChartOfAccount) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChartOfAccount{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", accountType='" + getAccountType() + "'" +
            ", code='" + getCode() + "'" +
            ", initialBalance=" + getInitialBalance() +
            ", currentBalance=" + getCurrentBalance() +
            "}";
    }
}
