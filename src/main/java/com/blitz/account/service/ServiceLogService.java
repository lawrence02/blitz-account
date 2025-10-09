package com.blitz.account.service;

import com.blitz.account.domain.ServiceLog;
import com.blitz.account.repository.ServiceLogRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.ServiceLog}.
 */
@Service
@Transactional
public class ServiceLogService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceLogService.class);

    private final ServiceLogRepository serviceLogRepository;

    public ServiceLogService(ServiceLogRepository serviceLogRepository) {
        this.serviceLogRepository = serviceLogRepository;
    }

    /**
     * Save a serviceLog.
     *
     * @param serviceLog the entity to save.
     * @return the persisted entity.
     */
    public ServiceLog save(ServiceLog serviceLog) {
        LOG.debug("Request to save ServiceLog : {}", serviceLog);
        return serviceLogRepository.save(serviceLog);
    }

    /**
     * Update a serviceLog.
     *
     * @param serviceLog the entity to save.
     * @return the persisted entity.
     */
    public ServiceLog update(ServiceLog serviceLog) {
        LOG.debug("Request to update ServiceLog : {}", serviceLog);
        return serviceLogRepository.save(serviceLog);
    }

    /**
     * Partially update a serviceLog.
     *
     * @param serviceLog the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServiceLog> partialUpdate(ServiceLog serviceLog) {
        LOG.debug("Request to partially update ServiceLog : {}", serviceLog);

        return serviceLogRepository
            .findById(serviceLog.getId())
            .map(existingServiceLog -> {
                if (serviceLog.getVehicleId() != null) {
                    existingServiceLog.setVehicleId(serviceLog.getVehicleId());
                }
                if (serviceLog.getServiceDate() != null) {
                    existingServiceLog.setServiceDate(serviceLog.getServiceDate());
                }
                if (serviceLog.getDescription() != null) {
                    existingServiceLog.setDescription(serviceLog.getDescription());
                }
                if (serviceLog.getCost() != null) {
                    existingServiceLog.setCost(serviceLog.getCost());
                }
                if (serviceLog.getMileageAtService() != null) {
                    existingServiceLog.setMileageAtService(serviceLog.getMileageAtService());
                }
                if (serviceLog.getSupplierId() != null) {
                    existingServiceLog.setSupplierId(serviceLog.getSupplierId());
                }

                return existingServiceLog;
            })
            .map(serviceLogRepository::save);
    }

    /**
     * Get all the serviceLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceLog> findAll(Pageable pageable) {
        LOG.debug("Request to get all ServiceLogs");
        return serviceLogRepository.findAll(pageable);
    }

    /**
     * Get one serviceLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceLog> findOne(Long id) {
        LOG.debug("Request to get ServiceLog : {}", id);
        return serviceLogRepository.findById(id);
    }

    /**
     * Delete the serviceLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceLog : {}", id);
        serviceLogRepository.deleteById(id);
    }
}
