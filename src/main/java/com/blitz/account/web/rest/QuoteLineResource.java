package com.blitz.account.web.rest;

import com.blitz.account.domain.QuoteLine;
import com.blitz.account.repository.QuoteLineRepository;
import com.blitz.account.service.QuoteLineService;
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
 * REST controller for managing {@link com.blitz.account.domain.QuoteLine}.
 */
@RestController
@RequestMapping("/api/quote-lines")
public class QuoteLineResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteLineResource.class);

    private static final String ENTITY_NAME = "quoteLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuoteLineService quoteLineService;

    private final QuoteLineRepository quoteLineRepository;

    public QuoteLineResource(QuoteLineService quoteLineService, QuoteLineRepository quoteLineRepository) {
        this.quoteLineService = quoteLineService;
        this.quoteLineRepository = quoteLineRepository;
    }

    /**
     * {@code POST  /quote-lines} : Create a new quoteLine.
     *
     * @param quoteLine the quoteLine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quoteLine, or with status {@code 400 (Bad Request)} if the quoteLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuoteLine> createQuoteLine(@Valid @RequestBody QuoteLine quoteLine) throws URISyntaxException {
        LOG.debug("REST request to save QuoteLine : {}", quoteLine);
        if (quoteLine.getId() != null) {
            throw new BadRequestAlertException("A new quoteLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quoteLine = quoteLineService.save(quoteLine);
        return ResponseEntity.created(new URI("/api/quote-lines/" + quoteLine.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quoteLine.getId().toString()))
            .body(quoteLine);
    }

    /**
     * {@code PUT  /quote-lines/:id} : Updates an existing quoteLine.
     *
     * @param id the id of the quoteLine to save.
     * @param quoteLine the quoteLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteLine,
     * or with status {@code 400 (Bad Request)} if the quoteLine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quoteLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuoteLine> updateQuoteLine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuoteLine quoteLine
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuoteLine : {}, {}", id, quoteLine);
        if (quoteLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quoteLine = quoteLineService.update(quoteLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteLine.getId().toString()))
            .body(quoteLine);
    }

    /**
     * {@code PATCH  /quote-lines/:id} : Partial updates given fields of an existing quoteLine, field will ignore if it is null
     *
     * @param id the id of the quoteLine to save.
     * @param quoteLine the quoteLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quoteLine,
     * or with status {@code 400 (Bad Request)} if the quoteLine is not valid,
     * or with status {@code 404 (Not Found)} if the quoteLine is not found,
     * or with status {@code 500 (Internal Server Error)} if the quoteLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuoteLine> partialUpdateQuoteLine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuoteLine quoteLine
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuoteLine partially : {}, {}", id, quoteLine);
        if (quoteLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quoteLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quoteLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuoteLine> result = quoteLineService.partialUpdate(quoteLine);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quoteLine.getId().toString())
        );
    }

    /**
     * {@code GET  /quote-lines} : get all the quoteLines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quoteLines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuoteLine>> getAllQuoteLines(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of QuoteLines");
        Page<QuoteLine> page = quoteLineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quote-lines/:id} : get the "id" quoteLine.
     *
     * @param id the id of the quoteLine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quoteLine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuoteLine> getQuoteLine(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuoteLine : {}", id);
        Optional<QuoteLine> quoteLine = quoteLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quoteLine);
    }

    /**
     * {@code DELETE  /quote-lines/:id} : delete the "id" quoteLine.
     *
     * @param id the id of the quoteLine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuoteLine(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuoteLine : {}", id);
        quoteLineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
