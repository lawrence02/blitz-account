package com.blitz.account.web.rest;

import com.blitz.account.domain.IncidentLog;
import com.blitz.account.repository.IncidentLogRepository;
import com.blitz.account.service.IncidentLogService;
import com.blitz.account.service.dto.IncidentStatsDTO;
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
 * REST controller for managing {@link com.blitz.account.domain.IncidentLog}.
 */
@RestController
@RequestMapping("/api/incident-logs")
public class IncidentLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(IncidentLogResource.class);

    private static final String ENTITY_NAME = "incidentLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncidentLogService incidentLogService;

    private final IncidentLogRepository incidentLogRepository;

    public IncidentLogResource(IncidentLogService incidentLogService, IncidentLogRepository incidentLogRepository) {
        this.incidentLogService = incidentLogService;
        this.incidentLogRepository = incidentLogRepository;
    }

    /**
     * {@code POST  /incident-logs} : Create a new incidentLog.
     *
     * @param incidentLog the incidentLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incidentLog, or with status {@code 400 (Bad Request)} if the incidentLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IncidentLog> createIncidentLog(@Valid @RequestBody IncidentLog incidentLog) throws URISyntaxException {
        LOG.debug("REST request to save IncidentLog : {}", incidentLog);
        if (incidentLog.getId() != null) {
            throw new BadRequestAlertException("A new incidentLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        incidentLog = incidentLogService.save(incidentLog);
        return ResponseEntity.created(new URI("/api/incident-logs/" + incidentLog.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, incidentLog.getId().toString()))
            .body(incidentLog);
    }

    /**
     * {@code PUT  /incident-logs/:id} : Updates an existing incidentLog.
     *
     * @param id the id of the incidentLog to save.
     * @param incidentLog the incidentLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incidentLog,
     * or with status {@code 400 (Bad Request)} if the incidentLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incidentLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IncidentLog> updateIncidentLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IncidentLog incidentLog
    ) throws URISyntaxException {
        LOG.debug("REST request to update IncidentLog : {}, {}", id, incidentLog);
        if (incidentLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incidentLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incidentLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        incidentLog = incidentLogService.update(incidentLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incidentLog.getId().toString()))
            .body(incidentLog);
    }

    /**
     * {@code PATCH  /incident-logs/:id} : Partial updates given fields of an existing incidentLog, field will ignore if it is null
     *
     * @param id the id of the incidentLog to save.
     * @param incidentLog the incidentLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incidentLog,
     * or with status {@code 400 (Bad Request)} if the incidentLog is not valid,
     * or with status {@code 404 (Not Found)} if the incidentLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the incidentLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IncidentLog> partialUpdateIncidentLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IncidentLog incidentLog
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IncidentLog partially : {}, {}", id, incidentLog);
        if (incidentLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incidentLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incidentLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IncidentLog> result = incidentLogService.partialUpdate(incidentLog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incidentLog.getId().toString())
        );
    }

    /**
     * {@code GET  /incident-logs} : get all the incidentLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incidentLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IncidentLog>> getAllIncidentLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of IncidentLogs");
        Page<IncidentLog> page = incidentLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /incident-logs/:id} : get the "id" incidentLog.
     *
     * @param id the id of the incidentLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incidentLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IncidentLog> getIncidentLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IncidentLog : {}", id);
        Optional<IncidentLog> incidentLog = incidentLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incidentLog);
    }

    /**
     * {@code DELETE  /incident-logs/:id} : delete the "id" incidentLog.
     *
     * @param id the id of the incidentLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncidentLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IncidentLog : {}", id);
        incidentLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/incident-stats")
    public IncidentStatsDTO getIncidentStats() {
        return incidentLogService.getIncidentStats();
    }
}
