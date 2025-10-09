package com.blitz.account.web.rest;

import com.blitz.account.domain.ExchangeRate;
import com.blitz.account.repository.ExchangeRateRepository;
import com.blitz.account.service.ExchangeRateService;
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
 * REST controller for managing {@link com.blitz.account.domain.ExchangeRate}.
 */
@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateResource.class);

    private static final String ENTITY_NAME = "exchangeRate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExchangeRateService exchangeRateService;

    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateResource(ExchangeRateService exchangeRateService, ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateService = exchangeRateService;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    /**
     * {@code POST  /exchange-rates} : Create a new exchangeRate.
     *
     * @param exchangeRate the exchangeRate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exchangeRate, or with status {@code 400 (Bad Request)} if the exchangeRate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExchangeRate> createExchangeRate(@Valid @RequestBody ExchangeRate exchangeRate) throws URISyntaxException {
        LOG.debug("REST request to save ExchangeRate : {}", exchangeRate);
        if (exchangeRate.getId() != null) {
            throw new BadRequestAlertException("A new exchangeRate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        exchangeRate = exchangeRateService.save(exchangeRate);
        return ResponseEntity.created(new URI("/api/exchange-rates/" + exchangeRate.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, exchangeRate.getId().toString()))
            .body(exchangeRate);
    }

    /**
     * {@code PUT  /exchange-rates/:id} : Updates an existing exchangeRate.
     *
     * @param id the id of the exchangeRate to save.
     * @param exchangeRate the exchangeRate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exchangeRate,
     * or with status {@code 400 (Bad Request)} if the exchangeRate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exchangeRate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExchangeRate> updateExchangeRate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExchangeRate exchangeRate
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExchangeRate : {}, {}", id, exchangeRate);
        if (exchangeRate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exchangeRate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exchangeRateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        exchangeRate = exchangeRateService.update(exchangeRate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exchangeRate.getId().toString()))
            .body(exchangeRate);
    }

    /**
     * {@code PATCH  /exchange-rates/:id} : Partial updates given fields of an existing exchangeRate, field will ignore if it is null
     *
     * @param id the id of the exchangeRate to save.
     * @param exchangeRate the exchangeRate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exchangeRate,
     * or with status {@code 400 (Bad Request)} if the exchangeRate is not valid,
     * or with status {@code 404 (Not Found)} if the exchangeRate is not found,
     * or with status {@code 500 (Internal Server Error)} if the exchangeRate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExchangeRate> partialUpdateExchangeRate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExchangeRate exchangeRate
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExchangeRate partially : {}, {}", id, exchangeRate);
        if (exchangeRate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exchangeRate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exchangeRateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExchangeRate> result = exchangeRateService.partialUpdate(exchangeRate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exchangeRate.getId().toString())
        );
    }

    /**
     * {@code GET  /exchange-rates} : get all the exchangeRates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exchangeRates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ExchangeRates");
        Page<ExchangeRate> page = exchangeRateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exchange-rates/:id} : get the "id" exchangeRate.
     *
     * @param id the id of the exchangeRate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exchangeRate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExchangeRate> getExchangeRate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExchangeRate : {}", id);
        Optional<ExchangeRate> exchangeRate = exchangeRateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exchangeRate);
    }

    /**
     * {@code DELETE  /exchange-rates/:id} : delete the "id" exchangeRate.
     *
     * @param id the id of the exchangeRate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExchangeRate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExchangeRate : {}", id);
        exchangeRateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
