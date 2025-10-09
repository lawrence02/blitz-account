package com.blitz.account.repository;

import com.blitz.account.domain.ServiceLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceLogRepository extends JpaRepository<ServiceLog, Long> {}
