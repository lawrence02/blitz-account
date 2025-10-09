package com.blitz.account.service;

import com.blitz.account.domain.ChartOfAccount;
import com.blitz.account.repository.ChartOfAccountRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.ChartOfAccount}.
 */
@Service
@Transactional
public class ChartOfAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(ChartOfAccountService.class);

    private final ChartOfAccountRepository chartOfAccountRepository;

    public ChartOfAccountService(ChartOfAccountRepository chartOfAccountRepository) {
        this.chartOfAccountRepository = chartOfAccountRepository;
    }

    /**
     * Save a chartOfAccount.
     *
     * @param chartOfAccount the entity to save.
     * @return the persisted entity.
     */
    public ChartOfAccount save(ChartOfAccount chartOfAccount) {
        LOG.debug("Request to save ChartOfAccount : {}", chartOfAccount);
        return chartOfAccountRepository.save(chartOfAccount);
    }

    /**
     * Update a chartOfAccount.
     *
     * @param chartOfAccount the entity to save.
     * @return the persisted entity.
     */
    public ChartOfAccount update(ChartOfAccount chartOfAccount) {
        LOG.debug("Request to update ChartOfAccount : {}", chartOfAccount);
        return chartOfAccountRepository.save(chartOfAccount);
    }

    /**
     * Partially update a chartOfAccount.
     *
     * @param chartOfAccount the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChartOfAccount> partialUpdate(ChartOfAccount chartOfAccount) {
        LOG.debug("Request to partially update ChartOfAccount : {}", chartOfAccount);

        return chartOfAccountRepository
            .findById(chartOfAccount.getId())
            .map(existingChartOfAccount -> {
                if (chartOfAccount.getName() != null) {
                    existingChartOfAccount.setName(chartOfAccount.getName());
                }
                if (chartOfAccount.getAccountType() != null) {
                    existingChartOfAccount.setAccountType(chartOfAccount.getAccountType());
                }
                if (chartOfAccount.getCode() != null) {
                    existingChartOfAccount.setCode(chartOfAccount.getCode());
                }
                if (chartOfAccount.getInitialBalance() != null) {
                    existingChartOfAccount.setInitialBalance(chartOfAccount.getInitialBalance());
                }
                if (chartOfAccount.getCurrentBalance() != null) {
                    existingChartOfAccount.setCurrentBalance(chartOfAccount.getCurrentBalance());
                }

                return existingChartOfAccount;
            })
            .map(chartOfAccountRepository::save);
    }

    /**
     * Get one chartOfAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChartOfAccount> findOne(Long id) {
        LOG.debug("Request to get ChartOfAccount : {}", id);
        return chartOfAccountRepository.findById(id);
    }

    /**
     * Delete the chartOfAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ChartOfAccount : {}", id);
        chartOfAccountRepository.deleteById(id);
    }
}
