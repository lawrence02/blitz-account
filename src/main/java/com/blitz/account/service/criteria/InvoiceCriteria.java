package com.blitz.account.service.criteria;

import com.blitz.account.domain.enumeration.DocumentStatus;
import com.blitz.account.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.blitz.account.domain.Invoice} entity. This class is used
 * in {@link com.blitz.account.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DocumentStatus
     */
    public static class DocumentStatusFilter extends Filter<DocumentStatus> {

        public DocumentStatusFilter() {}

        public DocumentStatusFilter(DocumentStatusFilter filter) {
            super(filter);
        }

        @Override
        public DocumentStatusFilter copy() {
            return new DocumentStatusFilter(this);
        }
    }

    /**
     * Class for filtering PaymentStatus
     */
    public static class PaymentStatusFilter extends Filter<PaymentStatus> {

        public PaymentStatusFilter() {}

        public PaymentStatusFilter(PaymentStatusFilter filter) {
            super(filter);
        }

        @Override
        public PaymentStatusFilter copy() {
            return new PaymentStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter clientName;

    private InstantFilter issueDate;

    private InstantFilter dueDate;

    private DocumentStatusFilter status;

    private LongFilter currencyId;

    private LongFilter vatRateId;

    private BigDecimalFilter totalAmount;

    private BigDecimalFilter paidAmount;

    private PaymentStatusFilter paymentStatus;

    private Boolean distinct;

    public InvoiceCriteria() {}

    public InvoiceCriteria(InvoiceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.clientName = other.optionalClientName().map(StringFilter::copy).orElse(null);
        this.issueDate = other.optionalIssueDate().map(InstantFilter::copy).orElse(null);
        this.dueDate = other.optionalDueDate().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(DocumentStatusFilter::copy).orElse(null);
        this.currencyId = other.optionalCurrencyId().map(LongFilter::copy).orElse(null);
        this.vatRateId = other.optionalVatRateId().map(LongFilter::copy).orElse(null);
        this.totalAmount = other.optionalTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paidAmount = other.optionalPaidAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paymentStatus = other.optionalPaymentStatus().map(PaymentStatusFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
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

    public StringFilter getClientName() {
        return clientName;
    }

    public Optional<StringFilter> optionalClientName() {
        return Optional.ofNullable(clientName);
    }

    public StringFilter clientName() {
        if (clientName == null) {
            setClientName(new StringFilter());
        }
        return clientName;
    }

    public void setClientName(StringFilter clientName) {
        this.clientName = clientName;
    }

    public InstantFilter getIssueDate() {
        return issueDate;
    }

    public Optional<InstantFilter> optionalIssueDate() {
        return Optional.ofNullable(issueDate);
    }

    public InstantFilter issueDate() {
        if (issueDate == null) {
            setIssueDate(new InstantFilter());
        }
        return issueDate;
    }

    public void setIssueDate(InstantFilter issueDate) {
        this.issueDate = issueDate;
    }

    public InstantFilter getDueDate() {
        return dueDate;
    }

    public Optional<InstantFilter> optionalDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public InstantFilter dueDate() {
        if (dueDate == null) {
            setDueDate(new InstantFilter());
        }
        return dueDate;
    }

    public void setDueDate(InstantFilter dueDate) {
        this.dueDate = dueDate;
    }

    public DocumentStatusFilter getStatus() {
        return status;
    }

    public Optional<DocumentStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public DocumentStatusFilter status() {
        if (status == null) {
            setStatus(new DocumentStatusFilter());
        }
        return status;
    }

    public void setStatus(DocumentStatusFilter status) {
        this.status = status;
    }

    public LongFilter getCurrencyId() {
        return currencyId;
    }

    public Optional<LongFilter> optionalCurrencyId() {
        return Optional.ofNullable(currencyId);
    }

    public LongFilter currencyId() {
        if (currencyId == null) {
            setCurrencyId(new LongFilter());
        }
        return currencyId;
    }

    public void setCurrencyId(LongFilter currencyId) {
        this.currencyId = currencyId;
    }

    public LongFilter getVatRateId() {
        return vatRateId;
    }

    public Optional<LongFilter> optionalVatRateId() {
        return Optional.ofNullable(vatRateId);
    }

    public LongFilter vatRateId() {
        if (vatRateId == null) {
            setVatRateId(new LongFilter());
        }
        return vatRateId;
    }

    public void setVatRateId(LongFilter vatRateId) {
        this.vatRateId = vatRateId;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public Optional<BigDecimalFilter> optionalTotalAmount() {
        return Optional.ofNullable(totalAmount);
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            setTotalAmount(new BigDecimalFilter());
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimalFilter getPaidAmount() {
        return paidAmount;
    }

    public Optional<BigDecimalFilter> optionalPaidAmount() {
        return Optional.ofNullable(paidAmount);
    }

    public BigDecimalFilter paidAmount() {
        if (paidAmount == null) {
            setPaidAmount(new BigDecimalFilter());
        }
        return paidAmount;
    }

    public void setPaidAmount(BigDecimalFilter paidAmount) {
        this.paidAmount = paidAmount;
    }

    public PaymentStatusFilter getPaymentStatus() {
        return paymentStatus;
    }

    public Optional<PaymentStatusFilter> optionalPaymentStatus() {
        return Optional.ofNullable(paymentStatus);
    }

    public PaymentStatusFilter paymentStatus() {
        if (paymentStatus == null) {
            setPaymentStatus(new PaymentStatusFilter());
        }
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusFilter paymentStatus) {
        this.paymentStatus = paymentStatus;
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
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(clientName, that.clientName) &&
            Objects.equals(issueDate, that.issueDate) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(currencyId, that.currencyId) &&
            Objects.equals(vatRateId, that.vatRateId) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(paidAmount, that.paidAmount) &&
            Objects.equals(paymentStatus, that.paymentStatus) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            clientName,
            issueDate,
            dueDate,
            status,
            currencyId,
            vatRateId,
            totalAmount,
            paidAmount,
            paymentStatus,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalClientName().map(f -> "clientName=" + f + ", ").orElse("") +
            optionalIssueDate().map(f -> "issueDate=" + f + ", ").orElse("") +
            optionalDueDate().map(f -> "dueDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCurrencyId().map(f -> "currencyId=" + f + ", ").orElse("") +
            optionalVatRateId().map(f -> "vatRateId=" + f + ", ").orElse("") +
            optionalTotalAmount().map(f -> "totalAmount=" + f + ", ").orElse("") +
            optionalPaidAmount().map(f -> "paidAmount=" + f + ", ").orElse("") +
            optionalPaymentStatus().map(f -> "paymentStatus=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
