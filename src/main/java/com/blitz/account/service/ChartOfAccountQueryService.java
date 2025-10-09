package com.blitz.account.service;

import com.blitz.account.domain.*; // for static metamodels
import com.blitz.account.domain.ChartOfAccount;
import com.blitz.account.repository.ChartOfAccountRepository;
import com.blitz.account.service.criteria.ChartOfAccountCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ChartOfAccount} entities in the database.
 * The main input is a {@link ChartOfAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ChartOfAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChartOfAccountQueryService extends QueryService<ChartOfAccount> {

    private static final Logger LOG = LoggerFactory.getLogger(ChartOfAccountQueryService.class);

    private final ChartOfAccountRepository chartOfAccountRepository;

    public ChartOfAccountQueryService(ChartOfAccountRepository chartOfAccountRepository) {
        this.chartOfAccountRepository = chartOfAccountRepository;
    }

    /**
     * Return a {@link Page} of {@link ChartOfAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChartOfAccount> findByCriteria(ChartOfAccountCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChartOfAccount> specification = createSpecification(criteria);
        return chartOfAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChartOfAccountCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ChartOfAccount> specification = createSpecification(criteria);
        return chartOfAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link ChartOfAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChartOfAccount> createSpecification(ChartOfAccountCriteria criteria) {
        Specification<ChartOfAccount> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ChartOfAccount_.id),
                buildStringSpecification(criteria.getName(), ChartOfAccount_.name),
                buildSpecification(criteria.getAccountType(), ChartOfAccount_.accountType),
                buildStringSpecification(criteria.getCode(), ChartOfAccount_.code),
                buildRangeSpecification(criteria.getInitialBalance(), ChartOfAccount_.initialBalance),
                buildRangeSpecification(criteria.getCurrentBalance(), ChartOfAccount_.currentBalance)
            );
        }
        return specification;
    }
}
