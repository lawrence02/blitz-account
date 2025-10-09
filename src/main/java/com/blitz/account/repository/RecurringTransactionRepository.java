package com.blitz.account.repository;

import com.blitz.account.domain.RecurringTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecurringTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {}
