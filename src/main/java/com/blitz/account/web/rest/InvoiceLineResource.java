package com.blitz.account.web.rest;

import com.blitz.account.domain.InvoiceLine;
import com.blitz.account.repository.InvoiceLineRepository;
import com.blitz.account.service.InvoiceLineService;
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
 * REST controller for managing {@link com.blitz.account.domain.InvoiceLine}.
 */
@RestController
@RequestMapping("/api/invoice-lines")
public class InvoiceLineResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceLineResource.class);

    private static final String ENTITY_NAME = "invoiceLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceLineService invoiceLineService;

    private final InvoiceLineRepository invoiceLineRepository;

    public InvoiceLineResource(InvoiceLineService invoiceLineService, InvoiceLineRepository invoiceLineRepository) {
        this.invoiceLineService = invoiceLineService;
        this.invoiceLineRepository = invoiceLineRepository;
    }

    /**
     * {@code POST  /invoice-lines} : Create a new invoiceLine.
     *
     * @param invoiceLine the invoiceLine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceLine, or with status {@code 400 (Bad Request)} if the invoiceLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceLine> createInvoiceLine(@Valid @RequestBody InvoiceLine invoiceLine) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceLine : {}", invoiceLine);
        if (invoiceLine.getId() != null) {
            throw new BadRequestAlertException("A new invoiceLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceLine = invoiceLineService.save(invoiceLine);
        return ResponseEntity.created(new URI("/api/invoice-lines/" + invoiceLine.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceLine.getId().toString()))
            .body(invoiceLine);
    }

    /**
     * {@code PUT  /invoice-lines/:id} : Updates an existing invoiceLine.
     *
     * @param id the id of the invoiceLine to save.
     * @param invoiceLine the invoiceLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceLine,
     * or with status {@code 400 (Bad Request)} if the invoiceLine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceLine> updateInvoiceLine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceLine invoiceLine
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceLine : {}, {}", id, invoiceLine);
        if (invoiceLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceLine = invoiceLineService.update(invoiceLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceLine.getId().toString()))
            .body(invoiceLine);
    }

    /**
     * {@code PATCH  /invoice-lines/:id} : Partial updates given fields of an existing invoiceLine, field will ignore if it is null
     *
     * @param id the id of the invoiceLine to save.
     * @param invoiceLine the invoiceLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceLine,
     * or with status {@code 400 (Bad Request)} if the invoiceLine is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceLine is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceLine> partialUpdateInvoiceLine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceLine invoiceLine
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceLine partially : {}, {}", id, invoiceLine);
        if (invoiceLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceLine> result = invoiceLineService.partialUpdate(invoiceLine);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceLine.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-lines} : get all the invoiceLines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceLines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceLine>> getAllInvoiceLines(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of InvoiceLines");
        Page<InvoiceLine> page = invoiceLineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-lines/:id} : get the "id" invoiceLine.
     *
     * @param id the id of the invoiceLine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceLine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceLine> getInvoiceLine(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceLine : {}", id);
        Optional<InvoiceLine> invoiceLine = invoiceLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceLine);
    }

    /**
     * {@code DELETE  /invoice-lines/:id} : delete the "id" invoiceLine.
     *
     * @param id the id of the invoiceLine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceLine(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceLine : {}", id);
        invoiceLineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
