package com.blitz.account.service;

import com.blitz.account.domain.IncidentLog;
import com.blitz.account.domain.enumeration.IncidentType;
import com.blitz.account.repository.IncidentLogRepository;
import com.blitz.account.service.dto.IncidentStatsDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.IncidentLog}.
 */
@Service
@Transactional
public class IncidentLogService {

    private static final Logger LOG = LoggerFactory.getLogger(IncidentLogService.class);

    private final IncidentLogRepository incidentLogRepository;

    public IncidentLogService(IncidentLogRepository incidentLogRepository) {
        this.incidentLogRepository = incidentLogRepository;
    }

    /**
     * Save a incidentLog.
     *
     * @param incidentLog the entity to save.
     * @return the persisted entity.
     */
    public IncidentLog save(IncidentLog incidentLog) {
        LOG.debug("Request to save IncidentLog : {}", incidentLog);
        return incidentLogRepository.save(incidentLog);
    }

    /**
     * Update a incidentLog.
     *
     * @param incidentLog the entity to save.
     * @return the persisted entity.
     */
    public IncidentLog update(IncidentLog incidentLog) {
        LOG.debug("Request to update IncidentLog : {}", incidentLog);
        return incidentLogRepository.save(incidentLog);
    }

    /**
     * Partially update a incidentLog.
     *
     * @param incidentLog the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IncidentLog> partialUpdate(IncidentLog incidentLog) {
        LOG.debug("Request to partially update IncidentLog : {}", incidentLog);

        return incidentLogRepository
            .findById(incidentLog.getId())
            .map(existingIncidentLog -> {
                if (incidentLog.getVehicleId() != null) {
                    existingIncidentLog.setVehicleId(incidentLog.getVehicleId());
                }
                if (incidentLog.getTripId() != null) {
                    existingIncidentLog.setTripId(incidentLog.getTripId());
                }
                if (incidentLog.getIncidentDate() != null) {
                    existingIncidentLog.setIncidentDate(incidentLog.getIncidentDate());
                }
                if (incidentLog.getType() != null) {
                    existingIncidentLog.setType(incidentLog.getType());
                }
                if (incidentLog.getDescription() != null) {
                    existingIncidentLog.setDescription(incidentLog.getDescription());
                }
                if (incidentLog.getCost() != null) {
                    existingIncidentLog.setCost(incidentLog.getCost());
                }

                return existingIncidentLog;
            })
            .map(incidentLogRepository::save);
    }

    /**
     * Get all the incidentLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IncidentLog> findAll(Pageable pageable) {
        LOG.debug("Request to get all IncidentLogs");
        return incidentLogRepository.findAll(pageable);
    }

    /**
     * Get one incidentLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IncidentLog> findOne(Long id) {
        LOG.debug("Request to get IncidentLog : {}", id);
        return incidentLogRepository.findById(id);
    }

    /**
     * Delete the incidentLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IncidentLog : {}", id);
        incidentLogRepository.deleteById(id);
    }

    public IncidentStatsDTO getIncidentStats() {
        int accidents = incidentLogRepository.countByType(IncidentType.ACCIDENT);
        int dents = incidentLogRepository.countByType(IncidentType.DENT);
        int breakdowns = incidentLogRepository.countByType(IncidentType.BREAKDOWN);

        // Calculate incidents this month
        LocalDateTime now = LocalDateTime.now();
        Instant startOfMonth = now.withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant();
        Instant nowInstant = Instant.now();
        int thisMonth = (int) incidentLogRepository
            .findAll()
            .stream()
            .filter(i -> i.getIncidentDate().isAfter(startOfMonth) && i.getIncidentDate().isBefore(nowInstant))
            .count();

        return new IncidentStatsDTO(accidents, breakdowns, dents, thisMonth);
    }
}
