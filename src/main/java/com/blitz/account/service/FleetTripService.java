package com.blitz.account.service;

import com.blitz.account.domain.FleetTrip;
import com.blitz.account.repository.FleetTripRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.FleetTrip}.
 */
@Service
@Transactional
public class FleetTripService {

    private static final Logger LOG = LoggerFactory.getLogger(FleetTripService.class);

    private final FleetTripRepository fleetTripRepository;

    public FleetTripService(FleetTripRepository fleetTripRepository) {
        this.fleetTripRepository = fleetTripRepository;
    }

    /**
     * Save a fleetTrip.
     *
     * @param fleetTrip the entity to save.
     * @return the persisted entity.
     */
    public FleetTrip save(FleetTrip fleetTrip) {
        LOG.debug("Request to save FleetTrip : {}", fleetTrip);
        return fleetTripRepository.save(fleetTrip);
    }

    /**
     * Update a fleetTrip.
     *
     * @param fleetTrip the entity to save.
     * @return the persisted entity.
     */
    public FleetTrip update(FleetTrip fleetTrip) {
        LOG.debug("Request to update FleetTrip : {}", fleetTrip);
        return fleetTripRepository.save(fleetTrip);
    }

    /**
     * Partially update a fleetTrip.
     *
     * @param fleetTrip the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FleetTrip> partialUpdate(FleetTrip fleetTrip) {
        LOG.debug("Request to partially update FleetTrip : {}", fleetTrip);

        return fleetTripRepository
            .findById(fleetTrip.getId())
            .map(existingFleetTrip -> {
                if (fleetTrip.getVehicleId() != null) {
                    existingFleetTrip.setVehicleId(fleetTrip.getVehicleId());
                }
                if (fleetTrip.getDriverId() != null) {
                    existingFleetTrip.setDriverId(fleetTrip.getDriverId());
                }
                if (fleetTrip.getStartDate() != null) {
                    existingFleetTrip.setStartDate(fleetTrip.getStartDate());
                }
                if (fleetTrip.getEndDate() != null) {
                    existingFleetTrip.setEndDate(fleetTrip.getEndDate());
                }
                if (fleetTrip.getDistanceKm() != null) {
                    existingFleetTrip.setDistanceKm(fleetTrip.getDistanceKm());
                }
                if (fleetTrip.getStartLocation() != null) {
                    existingFleetTrip.setStartLocation(fleetTrip.getStartLocation());
                }
                if (fleetTrip.getEndLocation() != null) {
                    existingFleetTrip.setEndLocation(fleetTrip.getEndLocation());
                }
                if (fleetTrip.getLoadType() != null) {
                    existingFleetTrip.setLoadType(fleetTrip.getLoadType());
                }
                if (fleetTrip.getLoadDescription() != null) {
                    existingFleetTrip.setLoadDescription(fleetTrip.getLoadDescription());
                }
                if (fleetTrip.getRouteGeoCoordinates() != null) {
                    existingFleetTrip.setRouteGeoCoordinates(fleetTrip.getRouteGeoCoordinates());
                }
                if (fleetTrip.getTripCost() != null) {
                    existingFleetTrip.setTripCost(fleetTrip.getTripCost());
                }

                return existingFleetTrip;
            })
            .map(fleetTripRepository::save);
    }

    /**
     * Get one fleetTrip by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FleetTrip> findOne(Long id) {
        LOG.debug("Request to get FleetTrip : {}", id);
        return fleetTripRepository.findById(id);
    }

    /**
     * Delete the fleetTrip by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FleetTrip : {}", id);
        fleetTripRepository.deleteById(id);
    }
}
