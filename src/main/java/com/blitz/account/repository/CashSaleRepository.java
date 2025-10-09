package com.blitz.account.repository;

import com.blitz.account.domain.CashSale;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CashSale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashSaleRepository extends JpaRepository<CashSale, Long> {}
