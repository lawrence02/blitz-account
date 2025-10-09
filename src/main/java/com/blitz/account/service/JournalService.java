package com.blitz.account.service;

import com.blitz.account.domain.Journal;
import com.blitz.account.repository.JournalRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.Journal}.
 */
@Service
@Transactional
public class JournalService {

    private static final Logger LOG = LoggerFactory.getLogger(JournalService.class);

    private final JournalRepository journalRepository;

    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    /**
     * Save a journal.
     *
     * @param journal the entity to save.
     * @return the persisted entity.
     */
    public Journal save(Journal journal) {
        LOG.debug("Request to save Journal : {}", journal);
        return journalRepository.save(journal);
    }

    /**
     * Update a journal.
     *
     * @param journal the entity to save.
     * @return the persisted entity.
     */
    public Journal update(Journal journal) {
        LOG.debug("Request to update Journal : {}", journal);
        return journalRepository.save(journal);
    }

    /**
     * Partially update a journal.
     *
     * @param journal the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Journal> partialUpdate(Journal journal) {
        LOG.debug("Request to partially update Journal : {}", journal);

        return journalRepository
            .findById(journal.getId())
            .map(existingJournal -> {
                if (journal.getJournalDate() != null) {
                    existingJournal.setJournalDate(journal.getJournalDate());
                }
                if (journal.getReference() != null) {
                    existingJournal.setReference(journal.getReference());
                }
                if (journal.getDescription() != null) {
                    existingJournal.setDescription(journal.getDescription());
                }

                return existingJournal;
            })
            .map(journalRepository::save);
    }

    /**
     * Get all the journals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Journal> findAll(Pageable pageable) {
        LOG.debug("Request to get all Journals");
        return journalRepository.findAll(pageable);
    }

    /**
     * Get one journal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Journal> findOne(Long id) {
        LOG.debug("Request to get Journal : {}", id);
        return journalRepository.findById(id);
    }

    /**
     * Delete the journal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Journal : {}", id);
        journalRepository.deleteById(id);
    }
}
