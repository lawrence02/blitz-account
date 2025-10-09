package com.blitz.account.web.rest;

import com.blitz.account.domain.VATRate;
import com.blitz.account.repository.VATRateRepository;
import com.blitz.account.service.VATRateService;
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
 * REST controller for managing {@link com.blitz.account.domain.VATRate}.
 */
@RestController
@RequestMapping("/api/vat-rates")
public class VATRateResource {

    private static final Logger LOG = LoggerFactory.getLogger(VATRateResource.class);

    private static final String ENTITY_NAME = "vATRate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VATRateService vATRateService;

    private final VATRateRepository vATRateRepository;

    public VATRateResource(VATRateService vATRateService, VATRateRepository vATRateRepository) {
        this.vATRateService = vATRateService;
        this.vATRateRepository = vATRateRepository;
    }

    /**
     * {@code POST  /vat-rates} : Create a new vATRate.
     *
     * @param vATRate the vATRate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vATRate, or with status {@code 400 (Bad Request)} if the vATRate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VATRate> createVATRate(@Valid @RequestBody VATRate vATRate) throws URISyntaxException {
        LOG.debug("REST request to save VATRate : {}", vATRate);
        if (vATRate.getId() != null) {
            throw new BadRequestAlertException("A new vATRate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vATRate = vATRateService.save(vATRate);
        return ResponseEntity.created(new URI("/api/vat-rates/" + vATRate.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vATRate.getId().toString()))
            .body(vATRate);
    }

    /**
     * {@code PUT  /vat-rates/:id} : Updates an existing vATRate.
     *
     * @param id the id of the vATRate to save.
     * @param vATRate the vATRate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vATRate,
     * or with status {@code 400 (Bad Request)} if the vATRate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vATRate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VATRate> updateVATRate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VATRate vATRate
    ) throws URISyntaxException {
        LOG.debug("REST request to update VATRate : {}, {}", id, vATRate);
        if (vATRate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vATRate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vATRateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vATRate = vATRateService.update(vATRate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vATRate.getId().toString()))
            .body(vATRate);
    }

    /**
     * {@code PATCH  /vat-rates/:id} : Partial updates given fields of an existing vATRate, field will ignore if it is null
     *
     * @param id the id of the vATRate to save.
     * @param vATRate the vATRate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vATRate,
     * or with status {@code 400 (Bad Request)} if the vATRate is not valid,
     * or with status {@code 404 (Not Found)} if the vATRate is not found,
     * or with status {@code 500 (Internal Server Error)} if the vATRate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VATRate> partialUpdateVATRate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VATRate vATRate
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VATRate partially : {}, {}", id, vATRate);
        if (vATRate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vATRate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vATRateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VATRate> result = vATRateService.partialUpdate(vATRate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vATRate.getId().toString())
        );
    }

    /**
     * {@code GET  /vat-rates} : get all the vATRates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vATRates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VATRate>> getAllVATRates(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of VATRates");
        Page<VATRate> page = vATRateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vat-rates/:id} : get the "id" vATRate.
     *
     * @param id the id of the vATRate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vATRate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VATRate> getVATRate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get VATRate : {}", id);
        Optional<VATRate> vATRate = vATRateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vATRate);
    }

    /**
     * {@code DELETE  /vat-rates/:id} : delete the "id" vATRate.
     *
     * @param id the id of the vATRate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVATRate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete VATRate : {}", id);
        vATRateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
