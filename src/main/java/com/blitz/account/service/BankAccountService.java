package com.blitz.account.service;

import com.blitz.account.domain.BankAccount;
import com.blitz.account.repository.BankAccountRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.BankAccount}.
 */
@Service
@Transactional
public class BankAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(BankAccountService.class);

    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    /**
     * Save a bankAccount.
     *
     * @param bankAccount the entity to save.
     * @return the persisted entity.
     */
    public BankAccount save(BankAccount bankAccount) {
        LOG.debug("Request to save BankAccount : {}", bankAccount);
        return bankAccountRepository.save(bankAccount);
    }

    /**
     * Update a bankAccount.
     *
     * @param bankAccount the entity to save.
     * @return the persisted entity.
     */
    public BankAccount update(BankAccount bankAccount) {
        LOG.debug("Request to update BankAccount : {}", bankAccount);
        return bankAccountRepository.save(bankAccount);
    }

    /**
     * Partially update a bankAccount.
     *
     * @param bankAccount the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BankAccount> partialUpdate(BankAccount bankAccount) {
        LOG.debug("Request to partially update BankAccount : {}", bankAccount);

        return bankAccountRepository
            .findById(bankAccount.getId())
            .map(existingBankAccount -> {
                if (bankAccount.getName() != null) {
                    existingBankAccount.setName(bankAccount.getName());
                }
                if (bankAccount.getAccountNumber() != null) {
                    existingBankAccount.setAccountNumber(bankAccount.getAccountNumber());
                }
                if (bankAccount.getBankName() != null) {
                    existingBankAccount.setBankName(bankAccount.getBankName());
                }

                return existingBankAccount;
            })
            .map(bankAccountRepository::save);
    }

    /**
     * Get one bankAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BankAccount> findOne(Long id) {
        LOG.debug("Request to get BankAccount : {}", id);
        return bankAccountRepository.findById(id);
    }

    /**
     * Delete the bankAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BankAccount : {}", id);
        bankAccountRepository.deleteById(id);
    }
}
