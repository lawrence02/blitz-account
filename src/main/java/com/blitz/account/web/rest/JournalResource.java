package com.blitz.account.web.rest;

import com.blitz.account.domain.Journal;
import com.blitz.account.repository.JournalRepository;
import com.blitz.account.service.JournalService;
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
 * REST controller for managing {@link com.blitz.account.domain.Journal}.
 */
@RestController
@RequestMapping("/api/journals")
public class JournalResource {

    private static final Logger LOG = LoggerFactory.getLogger(JournalResource.class);

    private static final String ENTITY_NAME = "journal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JournalService journalService;

    private final JournalRepository journalRepository;

    public JournalResource(JournalService journalService, JournalRepository journalRepository) {
        this.journalService = journalService;
        this.journalRepository = journalRepository;
    }

    /**
     * {@code POST  /journals} : Create a new journal.
     *
     * @param journal the journal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new journal, or with status {@code 400 (Bad Request)} if the journal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Journal> createJournal(@Valid @RequestBody Journal journal) throws URISyntaxException {
        LOG.debug("REST request to save Journal : {}", journal);
        if (journal.getId() != null) {
            throw new BadRequestAlertException("A new journal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        journal = journalService.save(journal);
        return ResponseEntity.created(new URI("/api/journals/" + journal.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, journal.getId().toString()))
            .body(journal);
    }

    /**
     * {@code PUT  /journals/:id} : Updates an existing journal.
     *
     * @param id the id of the journal to save.
     * @param journal the journal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated journal,
     * or with status {@code 400 (Bad Request)} if the journal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the journal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Journal> updateJournal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Journal journal
    ) throws URISyntaxException {
        LOG.debug("REST request to update Journal : {}, {}", id, journal);
        if (journal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, journal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!journalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        journal = journalService.update(journal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, journal.getId().toString()))
            .body(journal);
    }

    /**
     * {@code PATCH  /journals/:id} : Partial updates given fields of an existing journal, field will ignore if it is null
     *
     * @param id the id of the journal to save.
     * @param journal the journal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated journal,
     * or with status {@code 400 (Bad Request)} if the journal is not valid,
     * or with status {@code 404 (Not Found)} if the journal is not found,
     * or with status {@code 500 (Internal Server Error)} if the journal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Journal> partialUpdateJournal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Journal journal
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Journal partially : {}, {}", id, journal);
        if (journal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, journal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!journalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Journal> result = journalService.partialUpdate(journal);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, journal.getId().toString())
        );
    }

    /**
     * {@code GET  /journals} : get all the journals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of journals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Journal>> getAllJournals(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Journals");
        Page<Journal> page = journalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /journals/:id} : get the "id" journal.
     *
     * @param id the id of the journal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the journal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Journal> getJournal(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Journal : {}", id);
        Optional<Journal> journal = journalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(journal);
    }

    /**
     * {@code DELETE  /journals/:id} : delete the "id" journal.
     *
     * @param id the id of the journal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJournal(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Journal : {}", id);
        journalService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
