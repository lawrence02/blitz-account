package com.blitz.account.service.dto;

import java.math.BigDecimal;

public class InvoiceStatsDTO {

    private long outstanding;
    private long overdue;
    private BigDecimal totalAmount;
    private BigDecimal overdueAmount;

    public InvoiceStatsDTO() {}

    public InvoiceStatsDTO(long outstanding, long overdue, BigDecimal totalAmount, BigDecimal overdueAmount) {
        this.outstanding = outstanding;
        this.overdue = overdue;
        this.totalAmount = totalAmount;
        this.overdueAmount = overdueAmount;
    }

    public long getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(long outstanding) {
        this.outstanding = outstanding;
    }

    public long getOverdue() {
        return overdue;
    }

    public void setOverdue(long overdue) {
        this.overdue = overdue;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    @Override
    public String toString() {
        return (
            "InvoiceStatsDTO [outstanding=" +
            outstanding +
            ", overdue=" +
            overdue +
            ", totalAmount=" +
            totalAmount +
            ", overdueAmount=" +
            overdueAmount +
            "]"
        );
    }
}
