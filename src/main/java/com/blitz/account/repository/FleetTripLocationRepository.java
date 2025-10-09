package com.blitz.account.repository;

import com.blitz.account.domain.FleetTripLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FleetTripLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FleetTripLocationRepository extends JpaRepository<FleetTripLocation, Long> {}
