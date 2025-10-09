package com.blitz.account.service;

import com.blitz.account.domain.FleetTripLocation;
import com.blitz.account.repository.FleetTripLocationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.FleetTripLocation}.
 */
@Service
@Transactional
public class FleetTripLocationService {

    private static final Logger LOG = LoggerFactory.getLogger(FleetTripLocationService.class);

    private final FleetTripLocationRepository fleetTripLocationRepository;

    public FleetTripLocationService(FleetTripLocationRepository fleetTripLocationRepository) {
        this.fleetTripLocationRepository = fleetTripLocationRepository;
    }

    /**
     * Save a fleetTripLocation.
     *
     * @param fleetTripLocation the entity to save.
     * @return the persisted entity.
     */
    public FleetTripLocation save(FleetTripLocation fleetTripLocation) {
        LOG.debug("Request to save FleetTripLocation : {}", fleetTripLocation);
        return fleetTripLocationRepository.save(fleetTripLocation);
    }

    /**
     * Update a fleetTripLocation.
     *
     * @param fleetTripLocation the entity to save.
     * @return the persisted entity.
     */
    public FleetTripLocation update(FleetTripLocation fleetTripLocation) {
        LOG.debug("Request to update FleetTripLocation : {}", fleetTripLocation);
        return fleetTripLocationRepository.save(fleetTripLocation);
    }

    /**
     * Partially update a fleetTripLocation.
     *
     * @param fleetTripLocation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FleetTripLocation> partialUpdate(FleetTripLocation fleetTripLocation) {
        LOG.debug("Request to partially update FleetTripLocation : {}", fleetTripLocation);

        return fleetTripLocationRepository
            .findById(fleetTripLocation.getId())
            .map(existingFleetTripLocation -> {
                if (fleetTripLocation.getFleetTripId() != null) {
                    existingFleetTripLocation.setFleetTripId(fleetTripLocation.getFleetTripId());
                }
                if (fleetTripLocation.getTimestamp() != null) {
                    existingFleetTripLocation.setTimestamp(fleetTripLocation.getTimestamp());
                }
                if (fleetTripLocation.getLatitude() != null) {
                    existingFleetTripLocation.setLatitude(fleetTripLocation.getLatitude());
                }
                if (fleetTripLocation.getLongitude() != null) {
                    existingFleetTripLocation.setLongitude(fleetTripLocation.getLongitude());
                }
                if (fleetTripLocation.getSpeed() != null) {
                    existingFleetTripLocation.setSpeed(fleetTripLocation.getSpeed());
                }
                if (fleetTripLocation.getHeading() != null) {
                    existingFleetTripLocation.setHeading(fleetTripLocation.getHeading());
                }

                return existingFleetTripLocation;
            })
            .map(fleetTripLocationRepository::save);
    }

    /**
     * Get all the fleetTripLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FleetTripLocation> findAll(Pageable pageable) {
        LOG.debug("Request to get all FleetTripLocations");
        return fleetTripLocationRepository.findAll(pageable);
    }

    /**
     * Get one fleetTripLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FleetTripLocation> findOne(Long id) {
        LOG.debug("Request to get FleetTripLocation : {}", id);
        return fleetTripLocationRepository.findById(id);
    }

    /**
     * Delete the fleetTripLocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FleetTripLocation : {}", id);
        fleetTripLocationRepository.deleteById(id);
    }
}
