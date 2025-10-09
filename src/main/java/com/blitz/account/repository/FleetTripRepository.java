package com.blitz.account.repository;

import com.blitz.account.domain.FleetTrip;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FleetTrip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FleetTripRepository extends JpaRepository<FleetTrip, Long>, JpaSpecificationExecutor<FleetTrip> {}
