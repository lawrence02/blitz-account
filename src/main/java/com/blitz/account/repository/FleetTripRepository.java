package com.blitz.account.repository;

import com.blitz.account.domain.FleetTrip;
import java.time.Instant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FleetTrip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FleetTripRepository extends JpaRepository<FleetTrip, Long>, JpaSpecificationExecutor<FleetTrip> {
    @Query("SELECT COUNT(t) FROM FleetTrip t WHERE t.endDate IS NULL")
    long countActiveTrips();

    @Query("SELECT COUNT(t) FROM FleetTrip t WHERE FUNCTION('DATE', t.endDate) = FUNCTION('DATE', :today)")
    long countCompletedToday(Instant today);

    @Query("SELECT COUNT(t) FROM FleetTrip t WHERE FUNCTION('DATE', t.startDate) = FUNCTION('DATE', :tomorrow)")
    long countScheduledTomorrow(Instant tomorrow);
}
