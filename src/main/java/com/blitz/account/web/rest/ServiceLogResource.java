package com.blitz.account.web.rest;

import com.blitz.account.domain.ServiceLog;
import com.blitz.account.repository.ServiceLogRepository;
import com.blitz.account.service.ServiceLogService;
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
 * REST controller for managing {@link com.blitz.account.domain.ServiceLog}.
 */
@RestController
@RequestMapping("/api/service-logs")
public class ServiceLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceLogResource.class);

    private static final String ENTITY_NAME = "serviceLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceLogService serviceLogService;

    private final ServiceLogRepository serviceLogRepository;

    public ServiceLogResource(ServiceLogService serviceLogService, ServiceLogRepository serviceLogRepository) {
        this.serviceLogService = serviceLogService;
        this.serviceLogRepository = serviceLogRepository;
    }

    /**
     * {@code POST  /service-logs} : Create a new serviceLog.
     *
     * @param serviceLog the serviceLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceLog, or with status {@code 400 (Bad Request)} if the serviceLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceLog> createServiceLog(@Valid @RequestBody ServiceLog serviceLog) throws URISyntaxException {
        LOG.debug("REST request to save ServiceLog : {}", serviceLog);
        if (serviceLog.getId() != null) {
            throw new BadRequestAlertException("A new serviceLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceLog = serviceLogService.save(serviceLog);
        return ResponseEntity.created(new URI("/api/service-logs/" + serviceLog.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, serviceLog.getId().toString()))
            .body(serviceLog);
    }

    /**
     * {@code PUT  /service-logs/:id} : Updates an existing serviceLog.
     *
     * @param id the id of the serviceLog to save.
     * @param serviceLog the serviceLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceLog,
     * or with status {@code 400 (Bad Request)} if the serviceLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceLog> updateServiceLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceLog serviceLog
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceLog : {}, {}", id, serviceLog);
        if (serviceLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceLog = serviceLogService.update(serviceLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceLog.getId().toString()))
            .body(serviceLog);
    }

    /**
     * {@code PATCH  /service-logs/:id} : Partial updates given fields of an existing serviceLog, field will ignore if it is null
     *
     * @param id the id of the serviceLog to save.
     * @param serviceLog the serviceLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceLog,
     * or with status {@code 400 (Bad Request)} if the serviceLog is not valid,
     * or with status {@code 404 (Not Found)} if the serviceLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceLog> partialUpdateServiceLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceLog serviceLog
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServiceLog partially : {}, {}", id, serviceLog);
        if (serviceLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceLog> result = serviceLogService.partialUpdate(serviceLog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceLog.getId().toString())
        );
    }

    /**
     * {@code GET  /service-logs} : get all the serviceLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceLog>> getAllServiceLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ServiceLogs");
        Page<ServiceLog> page = serviceLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-logs/:id} : get the "id" serviceLog.
     *
     * @param id the id of the serviceLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceLog> getServiceLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceLog : {}", id);
        Optional<ServiceLog> serviceLog = serviceLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceLog);
    }

    /**
     * {@code DELETE  /service-logs/:id} : delete the "id" serviceLog.
     *
     * @param id the id of the serviceLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceLog : {}", id);
        serviceLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
