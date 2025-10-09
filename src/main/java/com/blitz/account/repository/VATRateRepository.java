package com.blitz.account.repository;

import com.blitz.account.domain.VATRate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VATRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VATRateRepository extends JpaRepository<VATRate, Long> {}
