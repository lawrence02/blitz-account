package com.blitz.account.repository;

import com.blitz.account.domain.FuelLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FuelLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FuelLogRepository extends JpaRepository<FuelLog, Long> {}
