package com.blitz.account.web.rest;

import com.blitz.account.domain.BankTransaction;
import com.blitz.account.repository.BankTransactionRepository;
import com.blitz.account.service.BankTransactionService;
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
 * REST controller for managing {@link com.blitz.account.domain.BankTransaction}.
 */
@RestController
@RequestMapping("/api/bank-transactions")
public class BankTransactionResource {

    private static final Logger LOG = LoggerFactory.getLogger(BankTransactionResource.class);

    private static final String ENTITY_NAME = "bankTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankTransactionService bankTransactionService;

    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionResource(BankTransactionService bankTransactionService, BankTransactionRepository bankTransactionRepository) {
        this.bankTransactionService = bankTransactionService;
        this.bankTransactionRepository = bankTransactionRepository;
    }

    /**
     * {@code POST  /bank-transactions} : Create a new bankTransaction.
     *
     * @param bankTransaction the bankTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankTransaction, or with status {@code 400 (Bad Request)} if the bankTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BankTransaction> createBankTransaction(@Valid @RequestBody BankTransaction bankTransaction)
        throws URISyntaxException {
        LOG.debug("REST request to save BankTransaction : {}", bankTransaction);
        if (bankTransaction.getId() != null) {
            throw new BadRequestAlertException("A new bankTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bankTransaction = bankTransactionService.save(bankTransaction);
        return ResponseEntity.created(new URI("/api/bank-transactions/" + bankTransaction.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bankTransaction.getId().toString()))
            .body(bankTransaction);
    }

    /**
     * {@code PUT  /bank-transactions/:id} : Updates an existing bankTransaction.
     *
     * @param id the id of the bankTransaction to save.
     * @param bankTransaction the bankTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankTransaction,
     * or with status {@code 400 (Bad Request)} if the bankTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BankTransaction> updateBankTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BankTransaction bankTransaction
    ) throws URISyntaxException {
        LOG.debug("REST request to update BankTransaction : {}, {}", id, bankTransaction);
        if (bankTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bankTransaction = bankTransactionService.update(bankTransaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankTransaction.getId().toString()))
            .body(bankTransaction);
    }

    /**
     * {@code PATCH  /bank-transactions/:id} : Partial updates given fields of an existing bankTransaction, field will ignore if it is null
     *
     * @param id the id of the bankTransaction to save.
     * @param bankTransaction the bankTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankTransaction,
     * or with status {@code 400 (Bad Request)} if the bankTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the bankTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the bankTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BankTransaction> partialUpdateBankTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BankTransaction bankTransaction
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BankTransaction partially : {}, {}", id, bankTransaction);
        if (bankTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BankTransaction> result = bankTransactionService.partialUpdate(bankTransaction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /bank-transactions} : get all the bankTransactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankTransactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BankTransaction>> getAllBankTransactions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of BankTransactions");
        Page<BankTransaction> page = bankTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bank-transactions/:id} : get the "id" bankTransaction.
     *
     * @param id the id of the bankTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BankTransaction> getBankTransaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BankTransaction : {}", id);
        Optional<BankTransaction> bankTransaction = bankTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankTransaction);
    }

    /**
     * {@code DELETE  /bank-transactions/:id} : delete the "id" bankTransaction.
     *
     * @param id the id of the bankTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankTransaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BankTransaction : {}", id);
        bankTransactionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
