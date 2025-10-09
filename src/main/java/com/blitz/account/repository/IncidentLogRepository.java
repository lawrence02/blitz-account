package com.blitz.account.repository;

import com.blitz.account.domain.IncidentLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IncidentLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncidentLogRepository extends JpaRepository<IncidentLog, Long> {}
