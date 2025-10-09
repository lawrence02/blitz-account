package com.blitz.account.web.rest;

import com.blitz.account.domain.JournalLine;
import com.blitz.account.repository.JournalLineRepository;
import com.blitz.account.service.JournalLineService;
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
 * REST controller for managing {@link com.blitz.account.domain.JournalLine}.
 */
@RestController
@RequestMapping("/api/journal-lines")
public class JournalLineResource {

    private static final Logger LOG = LoggerFactory.getLogger(JournalLineResource.class);

    private static final String ENTITY_NAME = "journalLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JournalLineService journalLineService;

    private final JournalLineRepository journalLineRepository;

    public JournalLineResource(JournalLineService journalLineService, JournalLineRepository journalLineRepository) {
        this.journalLineService = journalLineService;
        this.journalLineRepository = journalLineRepository;
    }

    /**
     * {@code POST  /journal-lines} : Create a new journalLine.
     *
     * @param journalLine the journalLine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new journalLine, or with status {@code 400 (Bad Request)} if the journalLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<JournalLine> createJournalLine(@Valid @RequestBody JournalLine journalLine) throws URISyntaxException {
        LOG.debug("REST request to save JournalLine : {}", journalLine);
        if (journalLine.getId() != null) {
            throw new BadRequestAlertException("A new journalLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        journalLine = journalLineService.save(journalLine);
        return ResponseEntity.created(new URI("/api/journal-lines/" + journalLine.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, journalLine.getId().toString()))
            .body(journalLine);
    }

    /**
     * {@code PUT  /journal-lines/:id} : Updates an existing journalLine.
     *
     * @param id the id of the journalLine to save.
     * @param journalLine the journalLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated journalLine,
     * or with status {@code 400 (Bad Request)} if the journalLine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the journalLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JournalLine> updateJournalLine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JournalLine journalLine
    ) throws URISyntaxException {
        LOG.debug("REST request to update JournalLine : {}, {}", id, journalLine);
        if (journalLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, journalLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!journalLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        journalLine = journalLineService.update(journalLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, journalLine.getId().toString()))
            .body(journalLine);
    }

    /**
     * {@code PATCH  /journal-lines/:id} : Partial updates given fields of an existing journalLine, field will ignore if it is null
     *
     * @param id the id of the journalLine to save.
     * @param journalLine the journalLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated journalLine,
     * or with status {@code 400 (Bad Request)} if the journalLine is not valid,
     * or with status {@code 404 (Not Found)} if the journalLine is not found,
     * or with status {@code 500 (Internal Server Error)} if the journalLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JournalLine> partialUpdateJournalLine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JournalLine journalLine
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update JournalLine partially : {}, {}", id, journalLine);
        if (journalLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, journalLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!journalLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JournalLine> result = journalLineService.partialUpdate(journalLine);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, journalLine.getId().toString())
        );
    }

    /**
     * {@code GET  /journal-lines} : get all the journalLines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of journalLines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<JournalLine>> getAllJournalLines(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of JournalLines");
        Page<JournalLine> page = journalLineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /journal-lines/:id} : get the "id" journalLine.
     *
     * @param id the id of the journalLine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the journalLine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JournalLine> getJournalLine(@PathVariable("id") Long id) {
        LOG.debug("REST request to get JournalLine : {}", id);
        Optional<JournalLine> journalLine = journalLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(journalLine);
    }

    /**
     * {@code DELETE  /journal-lines/:id} : delete the "id" journalLine.
     *
     * @param id the id of the journalLine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJournalLine(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete JournalLine : {}", id);
        journalLineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
