package com.blitz.account.service;

import com.blitz.account.domain.RecurringTransaction;
import com.blitz.account.repository.RecurringTransactionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.RecurringTransaction}.
 */
@Service
@Transactional
public class RecurringTransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(RecurringTransactionService.class);

    private final RecurringTransactionRepository recurringTransactionRepository;

    public RecurringTransactionService(RecurringTransactionRepository recurringTransactionRepository) {
        this.recurringTransactionRepository = recurringTransactionRepository;
    }

    /**
     * Save a recurringTransaction.
     *
     * @param recurringTransaction the entity to save.
     * @return the persisted entity.
     */
    public RecurringTransaction save(RecurringTransaction recurringTransaction) {
        LOG.debug("Request to save RecurringTransaction : {}", recurringTransaction);
        return recurringTransactionRepository.save(recurringTransaction);
    }

    /**
     * Update a recurringTransaction.
     *
     * @param recurringTransaction the entity to save.
     * @return the persisted entity.
     */
    public RecurringTransaction update(RecurringTransaction recurringTransaction) {
        LOG.debug("Request to update RecurringTransaction : {}", recurringTransaction);
        return recurringTransactionRepository.save(recurringTransaction);
    }

    /**
     * Partially update a recurringTransaction.
     *
     * @param recurringTransaction the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecurringTransaction> partialUpdate(RecurringTransaction recurringTransaction) {
        LOG.debug("Request to partially update RecurringTransaction : {}", recurringTransaction);

        return recurringTransactionRepository
            .findById(recurringTransaction.getId())
            .map(existingRecurringTransaction -> {
                if (recurringTransaction.getName() != null) {
                    existingRecurringTransaction.setName(recurringTransaction.getName());
                }
                if (recurringTransaction.getAmount() != null) {
                    existingRecurringTransaction.setAmount(recurringTransaction.getAmount());
                }
                if (recurringTransaction.getFrequency() != null) {
                    existingRecurringTransaction.setFrequency(recurringTransaction.getFrequency());
                }
                if (recurringTransaction.getStartDate() != null) {
                    existingRecurringTransaction.setStartDate(recurringTransaction.getStartDate());
                }
                if (recurringTransaction.getEndDate() != null) {
                    existingRecurringTransaction.setEndDate(recurringTransaction.getEndDate());
                }
                if (recurringTransaction.getAccountId() != null) {
                    existingRecurringTransaction.setAccountId(recurringTransaction.getAccountId());
                }
                if (recurringTransaction.getCurrencyId() != null) {
                    existingRecurringTransaction.setCurrencyId(recurringTransaction.getCurrencyId());
                }
                if (recurringTransaction.getVatRateId() != null) {
                    existingRecurringTransaction.setVatRateId(recurringTransaction.getVatRateId());
                }

                return existingRecurringTransaction;
            })
            .map(recurringTransactionRepository::save);
    }

    /**
     * Get all the recurringTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RecurringTransaction> findAll(Pageable pageable) {
        LOG.debug("Request to get all RecurringTransactions");
        return recurringTransactionRepository.findAll(pageable);
    }

    /**
     * Get one recurringTransaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecurringTransaction> findOne(Long id) {
        LOG.debug("Request to get RecurringTransaction : {}", id);
        return recurringTransactionRepository.findById(id);
    }

    /**
     * Delete the recurringTransaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RecurringTransaction : {}", id);
        recurringTransactionRepository.deleteById(id);
    }
}
