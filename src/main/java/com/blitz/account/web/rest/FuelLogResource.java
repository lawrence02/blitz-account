package com.blitz.account.web.rest;

import com.blitz.account.domain.FuelLog;
import com.blitz.account.repository.FuelLogRepository;
import com.blitz.account.service.FuelLogService;
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
 * REST controller for managing {@link com.blitz.account.domain.FuelLog}.
 */
@RestController
@RequestMapping("/api/fuel-logs")
public class FuelLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(FuelLogResource.class);

    private static final String ENTITY_NAME = "fuelLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FuelLogService fuelLogService;

    private final FuelLogRepository fuelLogRepository;

    public FuelLogResource(FuelLogService fuelLogService, FuelLogRepository fuelLogRepository) {
        this.fuelLogService = fuelLogService;
        this.fuelLogRepository = fuelLogRepository;
    }

    /**
     * {@code POST  /fuel-logs} : Create a new fuelLog.
     *
     * @param fuelLog the fuelLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fuelLog, or with status {@code 400 (Bad Request)} if the fuelLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FuelLog> createFuelLog(@Valid @RequestBody FuelLog fuelLog) throws URISyntaxException {
        LOG.debug("REST request to save FuelLog : {}", fuelLog);
        if (fuelLog.getId() != null) {
            throw new BadRequestAlertException("A new fuelLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fuelLog = fuelLogService.save(fuelLog);
        return ResponseEntity.created(new URI("/api/fuel-logs/" + fuelLog.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fuelLog.getId().toString()))
            .body(fuelLog);
    }

    /**
     * {@code PUT  /fuel-logs/:id} : Updates an existing fuelLog.
     *
     * @param id the id of the fuelLog to save.
     * @param fuelLog the fuelLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fuelLog,
     * or with status {@code 400 (Bad Request)} if the fuelLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fuelLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FuelLog> updateFuelLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FuelLog fuelLog
    ) throws URISyntaxException {
        LOG.debug("REST request to update FuelLog : {}, {}", id, fuelLog);
        if (fuelLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fuelLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fuelLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fuelLog = fuelLogService.update(fuelLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fuelLog.getId().toString()))
            .body(fuelLog);
    }

    /**
     * {@code PATCH  /fuel-logs/:id} : Partial updates given fields of an existing fuelLog, field will ignore if it is null
     *
     * @param id the id of the fuelLog to save.
     * @param fuelLog the fuelLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fuelLog,
     * or with status {@code 400 (Bad Request)} if the fuelLog is not valid,
     * or with status {@code 404 (Not Found)} if the fuelLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the fuelLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FuelLog> partialUpdateFuelLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FuelLog fuelLog
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FuelLog partially : {}, {}", id, fuelLog);
        if (fuelLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fuelLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fuelLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FuelLog> result = fuelLogService.partialUpdate(fuelLog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fuelLog.getId().toString())
        );
    }

    /**
     * {@code GET  /fuel-logs} : get all the fuelLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fuelLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FuelLog>> getAllFuelLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of FuelLogs");
        Page<FuelLog> page = fuelLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fuel-logs/:id} : get the "id" fuelLog.
     *
     * @param id the id of the fuelLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fuelLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FuelLog> getFuelLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FuelLog : {}", id);
        Optional<FuelLog> fuelLog = fuelLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fuelLog);
    }

    /**
     * {@code DELETE  /fuel-logs/:id} : delete the "id" fuelLog.
     *
     * @param id the id of the fuelLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuelLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FuelLog : {}", id);
        fuelLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
