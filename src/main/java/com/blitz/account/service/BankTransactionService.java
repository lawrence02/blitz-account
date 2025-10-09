package com.blitz.account.service;

import com.blitz.account.domain.BankTransaction;
import com.blitz.account.repository.BankTransactionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.BankTransaction}.
 */
@Service
@Transactional
public class BankTransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(BankTransactionService.class);

    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionService(BankTransactionRepository bankTransactionRepository) {
        this.bankTransactionRepository = bankTransactionRepository;
    }

    /**
     * Save a bankTransaction.
     *
     * @param bankTransaction the entity to save.
     * @return the persisted entity.
     */
    public BankTransaction save(BankTransaction bankTransaction) {
        LOG.debug("Request to save BankTransaction : {}", bankTransaction);
        return bankTransactionRepository.save(bankTransaction);
    }

    /**
     * Update a bankTransaction.
     *
     * @param bankTransaction the entity to save.
     * @return the persisted entity.
     */
    public BankTransaction update(BankTransaction bankTransaction) {
        LOG.debug("Request to update BankTransaction : {}", bankTransaction);
        return bankTransactionRepository.save(bankTransaction);
    }

    /**
     * Partially update a bankTransaction.
     *
     * @param bankTransaction the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BankTransaction> partialUpdate(BankTransaction bankTransaction) {
        LOG.debug("Request to partially update BankTransaction : {}", bankTransaction);

        return bankTransactionRepository
            .findById(bankTransaction.getId())
            .map(existingBankTransaction -> {
                if (bankTransaction.getBankAccountId() != null) {
                    existingBankTransaction.setBankAccountId(bankTransaction.getBankAccountId());
                }
                if (bankTransaction.getTransactionDate() != null) {
                    existingBankTransaction.setTransactionDate(bankTransaction.getTransactionDate());
                }
                if (bankTransaction.getReference() != null) {
                    existingBankTransaction.setReference(bankTransaction.getReference());
                }
                if (bankTransaction.getAmount() != null) {
                    existingBankTransaction.setAmount(bankTransaction.getAmount());
                }
                if (bankTransaction.getDirection() != null) {
                    existingBankTransaction.setDirection(bankTransaction.getDirection());
                }
                if (bankTransaction.getRelatedPaymentId() != null) {
                    existingBankTransaction.setRelatedPaymentId(bankTransaction.getRelatedPaymentId());
                }
                if (bankTransaction.getDescription() != null) {
                    existingBankTransaction.setDescription(bankTransaction.getDescription());
                }

                return existingBankTransaction;
            })
            .map(bankTransactionRepository::save);
    }

    /**
     * Get all the bankTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BankTransaction> findAll(Pageable pageable) {
        LOG.debug("Request to get all BankTransactions");
        return bankTransactionRepository.findAll(pageable);
    }

    /**
     * Get one bankTransaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BankTransaction> findOne(Long id) {
        LOG.debug("Request to get BankTransaction : {}", id);
        return bankTransactionRepository.findById(id);
    }

    /**
     * Delete the bankTransaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BankTransaction : {}", id);
        bankTransactionRepository.deleteById(id);
    }
}
