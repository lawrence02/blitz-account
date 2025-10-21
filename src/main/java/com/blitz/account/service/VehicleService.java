package com.blitz.account.service;

import com.blitz.account.domain.Vehicle;
import com.blitz.account.domain.enumeration.VehicleStatus;
import com.blitz.account.repository.VehicleRepository;
import com.blitz.account.service.dto.VehicleStatsDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.Vehicle}.
 */
@Service
@Transactional
public class VehicleService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Save a vehicle.
     *
     * @param vehicle the entity to save.
     * @return the persisted entity.
     */
    public Vehicle save(Vehicle vehicle) {
        LOG.debug("Request to save Vehicle : {}", vehicle);
        return vehicleRepository.save(vehicle);
    }

    /**
     * Update a vehicle.
     *
     * @param vehicle the entity to save.
     * @return the persisted entity.
     */
    public Vehicle update(Vehicle vehicle) {
        LOG.debug("Request to update Vehicle : {}", vehicle);
        return vehicleRepository.save(vehicle);
    }

    /**
     * Partially update a vehicle.
     *
     * @param vehicle the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Vehicle> partialUpdate(Vehicle vehicle) {
        LOG.debug("Request to partially update Vehicle : {}", vehicle);

        return vehicleRepository
            .findById(vehicle.getId())
            .map(existingVehicle -> {
                if (vehicle.getName() != null) {
                    existingVehicle.setName(vehicle.getName());
                }
                if (vehicle.getLicensePlate() != null) {
                    existingVehicle.setLicensePlate(vehicle.getLicensePlate());
                }
                if (vehicle.getType() != null) {
                    existingVehicle.setType(vehicle.getType());
                }
                if (vehicle.getCurrentMileage() != null) {
                    existingVehicle.setCurrentMileage(vehicle.getCurrentMileage());
                }
                if (vehicle.getStatus() != null) {
                    existingVehicle.setStatus(vehicle.getStatus());
                }

                return existingVehicle;
            })
            .map(vehicleRepository::save);
    }

    /**
     * Get one vehicle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Vehicle> findOne(Long id) {
        LOG.debug("Request to get Vehicle : {}", id);
        return vehicleRepository.findById(id);
    }

    /**
     * Delete the vehicle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vehicle : {}", id);
        vehicleRepository.deleteById(id);
    }

    public VehicleStatsDTO getVehicleStats() {
        int available = vehicleRepository.countByStatus(VehicleStatus.AVAILABLE);
        int inTrip = vehicleRepository.countByStatus(VehicleStatus.IN_TRIP);
        int maintenance = vehicleRepository.countByStatus(VehicleStatus.MAINTENANCE);
        int idle = vehicleRepository.countByStatus(VehicleStatus.IDLE);

        return new VehicleStatsDTO(available, inTrip, maintenance, idle);
    }
}
