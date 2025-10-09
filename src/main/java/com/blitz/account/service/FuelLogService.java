package com.blitz.account.service;

import com.blitz.account.domain.FuelLog;
import com.blitz.account.repository.FuelLogRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.FuelLog}.
 */
@Service
@Transactional
public class FuelLogService {

    private static final Logger LOG = LoggerFactory.getLogger(FuelLogService.class);

    private final FuelLogRepository fuelLogRepository;

    public FuelLogService(FuelLogRepository fuelLogRepository) {
        this.fuelLogRepository = fuelLogRepository;
    }

    /**
     * Save a fuelLog.
     *
     * @param fuelLog the entity to save.
     * @return the persisted entity.
     */
    public FuelLog save(FuelLog fuelLog) {
        LOG.debug("Request to save FuelLog : {}", fuelLog);
        return fuelLogRepository.save(fuelLog);
    }

    /**
     * Update a fuelLog.
     *
     * @param fuelLog the entity to save.
     * @return the persisted entity.
     */
    public FuelLog update(FuelLog fuelLog) {
        LOG.debug("Request to update FuelLog : {}", fuelLog);
        return fuelLogRepository.save(fuelLog);
    }

    /**
     * Partially update a fuelLog.
     *
     * @param fuelLog the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FuelLog> partialUpdate(FuelLog fuelLog) {
        LOG.debug("Request to partially update FuelLog : {}", fuelLog);

        return fuelLogRepository
            .findById(fuelLog.getId())
            .map(existingFuelLog -> {
                if (fuelLog.getVehicleId() != null) {
                    existingFuelLog.setVehicleId(fuelLog.getVehicleId());
                }
                if (fuelLog.getDate() != null) {
                    existingFuelLog.setDate(fuelLog.getDate());
                }
                if (fuelLog.getFuelVolume() != null) {
                    existingFuelLog.setFuelVolume(fuelLog.getFuelVolume());
                }
                if (fuelLog.getFuelCost() != null) {
                    existingFuelLog.setFuelCost(fuelLog.getFuelCost());
                }
                if (fuelLog.getLocation() != null) {
                    existingFuelLog.setLocation(fuelLog.getLocation());
                }
                if (fuelLog.getTripId() != null) {
                    existingFuelLog.setTripId(fuelLog.getTripId());
                }

                return existingFuelLog;
            })
            .map(fuelLogRepository::save);
    }

    /**
     * Get all the fuelLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FuelLog> findAll(Pageable pageable) {
        LOG.debug("Request to get all FuelLogs");
        return fuelLogRepository.findAll(pageable);
    }

    /**
     * Get one fuelLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FuelLog> findOne(Long id) {
        LOG.debug("Request to get FuelLog : {}", id);
        return fuelLogRepository.findById(id);
    }

    /**
     * Delete the fuelLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FuelLog : {}", id);
        fuelLogRepository.deleteById(id);
    }
}
