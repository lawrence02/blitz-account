package com.blitz.account.web.rest;

import com.blitz.account.domain.CashSale;
import com.blitz.account.repository.CashSaleRepository;
import com.blitz.account.service.CashSaleService;
import com.blitz.account.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.blitz.account.domain.CashSale}.
 */
@RestController
@RequestMapping("/api/cash-sales")
public class CashSaleResource {

    private static final Logger LOG = LoggerFactory.getLogger(CashSaleResource.class);

    private static final String ENTITY_NAME = "cashSale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CashSaleService cashSaleService;

    private final CashSaleRepository cashSaleRepository;

    public CashSaleResource(CashSaleService cashSaleService, CashSaleRepository cashSaleRepository) {
        this.cashSaleService = cashSaleService;
        this.cashSaleRepository = cashSaleRepository;
    }

    /**
     * {@code POST  /cash-sales} : Create a new cashSale.
     *
     * @param cashSale the cashSale to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashSale, or with status {@code 400 (Bad Request)} if the cashSale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CashSale> createCashSale(@RequestBody CashSale cashSale) throws URISyntaxException {
        LOG.debug("REST request to save CashSale : {}", cashSale);
        if (cashSale.getId() != null) {
            throw new BadRequestAlertException("A new cashSale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cashSale = cashSaleService.save(cashSale);
        return ResponseEntity.created(new URI("/api/cash-sales/" + cashSale.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cashSale.getId().toString()))
            .body(cashSale);
    }

    /**
     * {@code PUT  /cash-sales/:id} : Updates an existing cashSale.
     *
     * @param id the id of the cashSale to save.
     * @param cashSale the cashSale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashSale,
     * or with status {@code 400 (Bad Request)} if the cashSale is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashSale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CashSale> updateCashSale(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CashSale cashSale
    ) throws URISyntaxException {
        LOG.debug("REST request to update CashSale : {}, {}", id, cashSale);
        if (cashSale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cashSale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cashSaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cashSale = cashSaleService.update(cashSale);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cashSale.getId().toString()))
            .body(cashSale);
    }

    /**
     * {@code PATCH  /cash-sales/:id} : Partial updates given fields of an existing cashSale, field will ignore if it is null
     *
     * @param id the id of the cashSale to save.
     * @param cashSale the cashSale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashSale,
     * or with status {@code 400 (Bad Request)} if the cashSale is not valid,
     * or with status {@code 404 (Not Found)} if the cashSale is not found,
     * or with status {@code 500 (Internal Server Error)} if the cashSale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CashSale> partialUpdateCashSale(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CashSale cashSale
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CashSale partially : {}, {}", id, cashSale);
        if (cashSale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cashSale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cashSaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CashSale> result = cashSaleService.partialUpdate(cashSale);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cashSale.getId().toString())
        );
    }

    /**
     * {@code GET  /cash-sales} : get all the cashSales.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashSales in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CashSale>> getAllCashSales(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CashSales");
        Page<CashSale> page = cashSaleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cash-sales/:id} : get the "id" cashSale.
     *
     * @param id the id of the cashSale to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashSale, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CashSale> getCashSale(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CashSale : {}", id);
        Optional<CashSale> cashSale = cashSaleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cashSale);
    }

    /**
     * {@code DELETE  /cash-sales/:id} : delete the "id" cashSale.
     *
     * @param id the id of the cashSale to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCashSale(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CashSale : {}", id);
        cashSaleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
