package com.blitz.account.service;

import com.blitz.account.domain.VATRate;
import com.blitz.account.repository.VATRateRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.VATRate}.
 */
@Service
@Transactional
public class VATRateService {

    private static final Logger LOG = LoggerFactory.getLogger(VATRateService.class);

    private final VATRateRepository vATRateRepository;

    public VATRateService(VATRateRepository vATRateRepository) {
        this.vATRateRepository = vATRateRepository;
    }

    /**
     * Save a vATRate.
     *
     * @param vATRate the entity to save.
     * @return the persisted entity.
     */
    public VATRate save(VATRate vATRate) {
        LOG.debug("Request to save VATRate : {}", vATRate);
        return vATRateRepository.save(vATRate);
    }

    /**
     * Update a vATRate.
     *
     * @param vATRate the entity to save.
     * @return the persisted entity.
     */
    public VATRate update(VATRate vATRate) {
        LOG.debug("Request to update VATRate : {}", vATRate);
        return vATRateRepository.save(vATRate);
    }

    /**
     * Partially update a vATRate.
     *
     * @param vATRate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VATRate> partialUpdate(VATRate vATRate) {
        LOG.debug("Request to partially update VATRate : {}", vATRate);

        return vATRateRepository
            .findById(vATRate.getId())
            .map(existingVATRate -> {
                if (vATRate.getName() != null) {
                    existingVATRate.setName(vATRate.getName());
                }
                if (vATRate.getPercentage() != null) {
                    existingVATRate.setPercentage(vATRate.getPercentage());
                }

                return existingVATRate;
            })
            .map(vATRateRepository::save);
    }

    /**
     * Get all the vATRates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VATRate> findAll(Pageable pageable) {
        LOG.debug("Request to get all VATRates");
        return vATRateRepository.findAll(pageable);
    }

    /**
     * Get one vATRate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VATRate> findOne(Long id) {
        LOG.debug("Request to get VATRate : {}", id);
        return vATRateRepository.findById(id);
    }

    /**
     * Delete the vATRate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete VATRate : {}", id);
        vATRateRepository.deleteById(id);
    }
}
