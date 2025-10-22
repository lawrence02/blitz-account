package com.blitz.account.repository;

import com.blitz.account.domain.IncidentLog;
import com.blitz.account.domain.enumeration.IncidentType;
import java.time.Instant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IncidentLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncidentLogRepository extends JpaRepository<IncidentLog, Long> {
    int countByType(IncidentType type);

    int countByTypeAndIncidentDateBetween(IncidentType type, Instant start, Instant end);

    default int countThisMonth() {
        Instant start = Instant.now().atZone(java.time.ZoneId.systemDefault()).withDayOfMonth(1).toInstant();
        Instant end = Instant.now();
        return (int) findAll().stream().filter(i -> i.getIncidentDate().isAfter(start) && i.getIncidentDate().isBefore(end)).count();
    }
}
