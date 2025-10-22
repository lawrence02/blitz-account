package com.blitz.account.repository;

import com.blitz.account.domain.Invoice;
import com.blitz.account.domain.enumeration.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    long countByPaymentStatusIn(PaymentStatus[] statuses);

    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i")
    BigDecimal findTotalAmount();

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.dueDate < :today AND i.paymentStatus <> 'PAID'")
    long countOverdue(Instant today);

    @Query(
        "SELECT COALESCE(SUM(i.totalAmount - COALESCE(i.paidAmount, 0)), 0) FROM Invoice i WHERE i.dueDate < :today AND i.paymentStatus <> 'PAID'"
    )
    BigDecimal sumOverdueAmount(Instant today);
}
