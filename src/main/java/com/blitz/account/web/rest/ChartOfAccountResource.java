package com.blitz.account.web.rest;

import com.blitz.account.domain.ChartOfAccount;
import com.blitz.account.repository.ChartOfAccountRepository;
import com.blitz.account.service.ChartOfAccountQueryService;
import com.blitz.account.service.ChartOfAccountService;
import com.blitz.account.service.criteria.ChartOfAccountCriteria;
import com.blitz.account.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.blitz.account.domain.ChartOfAccount}.
 */
@RestController
@RequestMapping("/api/chart-of-accounts")
public class ChartOfAccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChartOfAccountResource.class);

    private static final String ENTITY_NAME = "chartOfAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChartOfAccountService chartOfAccountService;

    private final ChartOfAccountRepository chartOfAccountRepository;

    private final ChartOfAccountQueryService chartOfAccountQueryService;

    public ChartOfAccountResource(
        ChartOfAccountService chartOfAccountService,
        ChartOfAccountRepository chartOfAccountRepository,
        ChartOfAccountQueryService chartOfAccountQueryService
    ) {
        this.chartOfAccountService = chartOfAccountService;
        this.chartOfAccountRepository = chartOfAccountRepository;
        this.chartOfAccountQueryService = chartOfAccountQueryService;
    }

    /**
     * {@code POST  /chart-of-accounts} : Create a new chartOfAccount.
     *
     * @param chartOfAccount the chartOfAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chartOfAccount, or with status {@code 400 (Bad Request)} if the chartOfAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ChartOfAccount> createChartOfAccount(@Valid @RequestBody ChartOfAccount chartOfAccount)
        throws URISyntaxException {
        LOG.debug("REST request to save ChartOfAccount : {}", chartOfAccount);
        if (chartOfAccount.getId() != null) {
            throw new BadRequestAlertException("A new chartOfAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        chartOfAccount = chartOfAccountService.save(chartOfAccount);
        return ResponseEntity.created(new URI("/api/chart-of-accounts/" + chartOfAccount.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, chartOfAccount.getId().toString()))
            .body(chartOfAccount);
    }

    /**
     * {@code PUT  /chart-of-accounts/:id} : Updates an existing chartOfAccount.
     *
     * @param id the id of the chartOfAccount to save.
     * @param chartOfAccount the chartOfAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chartOfAccount,
     * or with status {@code 400 (Bad Request)} if the chartOfAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chartOfAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChartOfAccount> updateChartOfAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChartOfAccount chartOfAccount
    ) throws URISyntaxException {
        LOG.debug("REST request to update ChartOfAccount : {}, {}", id, chartOfAccount);
        if (chartOfAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chartOfAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chartOfAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        chartOfAccount = chartOfAccountService.update(chartOfAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chartOfAccount.getId().toString()))
            .body(chartOfAccount);
    }

    /**
     * {@code PATCH  /chart-of-accounts/:id} : Partial updates given fields of an existing chartOfAccount, field will ignore if it is null
     *
     * @param id the id of the chartOfAccount to save.
     * @param chartOfAccount the chartOfAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chartOfAccount,
     * or with status {@code 400 (Bad Request)} if the chartOfAccount is not valid,
     * or with status {@code 404 (Not Found)} if the chartOfAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the chartOfAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChartOfAccount> partialUpdateChartOfAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChartOfAccount chartOfAccount
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ChartOfAccount partially : {}, {}", id, chartOfAccount);
        if (chartOfAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chartOfAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chartOfAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChartOfAccount> result = chartOfAccountService.partialUpdate(chartOfAccount);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chartOfAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /chart-of-accounts} : get all the chartOfAccounts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chartOfAccounts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ChartOfAccount>> getAllChartOfAccounts(
        ChartOfAccountCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ChartOfAccounts by criteria: {}", criteria);

        Page<ChartOfAccount> page = chartOfAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chart-of-accounts/count} : count all the chartOfAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countChartOfAccounts(ChartOfAccountCriteria criteria) {
        LOG.debug("REST request to count ChartOfAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(chartOfAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chart-of-accounts/:id} : get the "id" chartOfAccount.
     *
     * @param id the id of the chartOfAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chartOfAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChartOfAccount> getChartOfAccount(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ChartOfAccount : {}", id);
        Optional<ChartOfAccount> chartOfAccount = chartOfAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chartOfAccount);
    }

    /**
     * {@code DELETE  /chart-of-accounts/:id} : delete the "id" chartOfAccount.
     *
     * @param id the id of the chartOfAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChartOfAccount(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ChartOfAccount : {}", id);
        chartOfAccountService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
