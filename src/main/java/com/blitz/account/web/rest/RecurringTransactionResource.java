package com.blitz.account.web.rest;

import com.blitz.account.domain.RecurringTransaction;
import com.blitz.account.repository.RecurringTransactionRepository;
import com.blitz.account.service.RecurringTransactionService;
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
 * REST controller for managing {@link com.blitz.account.domain.RecurringTransaction}.
 */
@RestController
@RequestMapping("/api/recurring-transactions")
public class RecurringTransactionResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecurringTransactionResource.class);

    private static final String ENTITY_NAME = "recurringTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecurringTransactionService recurringTransactionService;

    private final RecurringTransactionRepository recurringTransactionRepository;

    public RecurringTransactionResource(
        RecurringTransactionService recurringTransactionService,
        RecurringTransactionRepository recurringTransactionRepository
    ) {
        this.recurringTransactionService = recurringTransactionService;
        this.recurringTransactionRepository = recurringTransactionRepository;
    }

    /**
     * {@code POST  /recurring-transactions} : Create a new recurringTransaction.
     *
     * @param recurringTransaction the recurringTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recurringTransaction, or with status {@code 400 (Bad Request)} if the recurringTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecurringTransaction> createRecurringTransaction(@Valid @RequestBody RecurringTransaction recurringTransaction)
        throws URISyntaxException {
        LOG.debug("REST request to save RecurringTransaction : {}", recurringTransaction);
        if (recurringTransaction.getId() != null) {
            throw new BadRequestAlertException("A new recurringTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recurringTransaction = recurringTransactionService.save(recurringTransaction);
        return ResponseEntity.created(new URI("/api/recurring-transactions/" + recurringTransaction.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, recurringTransaction.getId().toString()))
            .body(recurringTransaction);
    }

    /**
     * {@code PUT  /recurring-transactions/:id} : Updates an existing recurringTransaction.
     *
     * @param id the id of the recurringTransaction to save.
     * @param recurringTransaction the recurringTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recurringTransaction,
     * or with status {@code 400 (Bad Request)} if the recurringTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recurringTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecurringTransaction> updateRecurringTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecurringTransaction recurringTransaction
    ) throws URISyntaxException {
        LOG.debug("REST request to update RecurringTransaction : {}, {}", id, recurringTransaction);
        if (recurringTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recurringTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recurringTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recurringTransaction = recurringTransactionService.update(recurringTransaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recurringTransaction.getId().toString()))
            .body(recurringTransaction);
    }

    /**
     * {@code PATCH  /recurring-transactions/:id} : Partial updates given fields of an existing recurringTransaction, field will ignore if it is null
     *
     * @param id the id of the recurringTransaction to save.
     * @param recurringTransaction the recurringTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recurringTransaction,
     * or with status {@code 400 (Bad Request)} if the recurringTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the recurringTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the recurringTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecurringTransaction> partialUpdateRecurringTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecurringTransaction recurringTransaction
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RecurringTransaction partially : {}, {}", id, recurringTransaction);
        if (recurringTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recurringTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recurringTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecurringTransaction> result = recurringTransactionService.partialUpdate(recurringTransaction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recurringTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /recurring-transactions} : get all the recurringTransactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recurringTransactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecurringTransaction>> getAllRecurringTransactions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of RecurringTransactions");
        Page<RecurringTransaction> page = recurringTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recurring-transactions/:id} : get the "id" recurringTransaction.
     *
     * @param id the id of the recurringTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recurringTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecurringTransaction> getRecurringTransaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RecurringTransaction : {}", id);
        Optional<RecurringTransaction> recurringTransaction = recurringTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recurringTransaction);
    }

    /**
     * {@code DELETE  /recurring-transactions/:id} : delete the "id" recurringTransaction.
     *
     * @param id the id of the recurringTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecurringTransaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RecurringTransaction : {}", id);
        recurringTransactionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
