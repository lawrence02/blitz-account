package com.blitz.account.repository;

import com.blitz.account.domain.QuoteLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuoteLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuoteLineRepository extends JpaRepository<QuoteLine, Long> {}
