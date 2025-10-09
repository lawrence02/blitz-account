package com.blitz.account.service;

import com.blitz.account.domain.Budget;
import com.blitz.account.repository.BudgetRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.Budget}.
 */
@Service
@Transactional
public class BudgetService {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetService.class);

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    /**
     * Save a budget.
     *
     * @param budget the entity to save.
     * @return the persisted entity.
     */
    public Budget save(Budget budget) {
        LOG.debug("Request to save Budget : {}", budget);
        return budgetRepository.save(budget);
    }

    /**
     * Update a budget.
     *
     * @param budget the entity to save.
     * @return the persisted entity.
     */
    public Budget update(Budget budget) {
        LOG.debug("Request to update Budget : {}", budget);
        return budgetRepository.save(budget);
    }

    /**
     * Partially update a budget.
     *
     * @param budget the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Budget> partialUpdate(Budget budget) {
        LOG.debug("Request to partially update Budget : {}", budget);

        return budgetRepository
            .findById(budget.getId())
            .map(existingBudget -> {
                if (budget.getName() != null) {
                    existingBudget.setName(budget.getName());
                }
                if (budget.getStartDate() != null) {
                    existingBudget.setStartDate(budget.getStartDate());
                }
                if (budget.getEndDate() != null) {
                    existingBudget.setEndDate(budget.getEndDate());
                }
                if (budget.getAccountId() != null) {
                    existingBudget.setAccountId(budget.getAccountId());
                }
                if (budget.getAmount() != null) {
                    existingBudget.setAmount(budget.getAmount());
                }

                return existingBudget;
            })
            .map(budgetRepository::save);
    }

    /**
     * Get all the budgets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Budget> findAll(Pageable pageable) {
        LOG.debug("Request to get all Budgets");
        return budgetRepository.findAll(pageable);
    }

    /**
     * Get one budget by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Budget> findOne(Long id) {
        LOG.debug("Request to get Budget : {}", id);
        return budgetRepository.findById(id);
    }

    /**
     * Delete the budget by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Budget : {}", id);
        budgetRepository.deleteById(id);
    }
}
