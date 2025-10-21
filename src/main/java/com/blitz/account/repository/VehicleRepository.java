package com.blitz.account.repository;

import com.blitz.account.domain.Vehicle;
import com.blitz.account.domain.enumeration.VehicleStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vehicle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    int countByStatus(VehicleStatus status);
}
