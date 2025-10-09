package com.blitz.account.service.criteria;

import com.blitz.account.domain.enumeration.AccountType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.blitz.account.domain.ChartOfAccount} entity. This class is used
 * in {@link com.blitz.account.web.rest.ChartOfAccountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /chart-of-accounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChartOfAccountCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AccountType
     */
    public static class AccountTypeFilter extends Filter<AccountType> {

        public AccountTypeFilter() {}

        public AccountTypeFilter(AccountTypeFilter filter) {
            super(filter);
        }

        @Override
        public AccountTypeFilter copy() {
            return new AccountTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private AccountTypeFilter accountType;

    private StringFilter code;

    private BigDecimalFilter initialBalance;

    private BigDecimalFilter currentBalance;

    private Boolean distinct;

    public ChartOfAccountCriteria() {}

    public ChartOfAccountCriteria(ChartOfAccountCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.accountType = other.optionalAccountType().map(AccountTypeFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.initialBalance = other.optionalInitialBalance().map(BigDecimalFilter::copy).orElse(null);
        this.currentBalance = other.optionalCurrentBalance().map(BigDecimalFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ChartOfAccountCriteria copy() {
        return new ChartOfAccountCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public AccountTypeFilter getAccountType() {
        return accountType;
    }

    public Optional<AccountTypeFilter> optionalAccountType() {
        return Optional.ofNullable(accountType);
    }

    public AccountTypeFilter accountType() {
        if (accountType == null) {
            setAccountType(new AccountTypeFilter());
        }
        return accountType;
    }

    public void setAccountType(AccountTypeFilter accountType) {
        this.accountType = accountType;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public BigDecimalFilter getInitialBalance() {
        return initialBalance;
    }

    public Optional<BigDecimalFilter> optionalInitialBalance() {
        return Optional.ofNullable(initialBalance);
    }

    public BigDecimalFilter initialBalance() {
        if (initialBalance == null) {
            setInitialBalance(new BigDecimalFilter());
        }
        return initialBalance;
    }

    public void setInitialBalance(BigDecimalFilter initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimalFilter getCurrentBalance() {
        return currentBalance;
    }

    public Optional<BigDecimalFilter> optionalCurrentBalance() {
        return Optional.ofNullable(currentBalance);
    }

    public BigDecimalFilter currentBalance() {
        if (currentBalance == null) {
            setCurrentBalance(new BigDecimalFilter());
        }
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimalFilter currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChartOfAccountCriteria that = (ChartOfAccountCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(accountType, that.accountType) &&
            Objects.equals(code, that.code) &&
            Objects.equals(initialBalance, that.initialBalance) &&
            Objects.equals(currentBalance, that.currentBalance) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, accountType, code, initialBalance, currentBalance, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChartOfAccountCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalAccountType().map(f -> "accountType=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalInitialBalance().map(f -> "initialBalance=" + f + ", ").orElse("") +
            optionalCurrentBalance().map(f -> "currentBalance=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
