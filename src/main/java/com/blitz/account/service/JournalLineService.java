package com.blitz.account.service;

import com.blitz.account.domain.JournalLine;
import com.blitz.account.repository.JournalLineRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.JournalLine}.
 */
@Service
@Transactional
public class JournalLineService {

    private static final Logger LOG = LoggerFactory.getLogger(JournalLineService.class);

    private final JournalLineRepository journalLineRepository;

    public JournalLineService(JournalLineRepository journalLineRepository) {
        this.journalLineRepository = journalLineRepository;
    }

    /**
     * Save a journalLine.
     *
     * @param journalLine the entity to save.
     * @return the persisted entity.
     */
    public JournalLine save(JournalLine journalLine) {
        LOG.debug("Request to save JournalLine : {}", journalLine);
        return journalLineRepository.save(journalLine);
    }

    /**
     * Update a journalLine.
     *
     * @param journalLine the entity to save.
     * @return the persisted entity.
     */
    public JournalLine update(JournalLine journalLine) {
        LOG.debug("Request to update JournalLine : {}", journalLine);
        return journalLineRepository.save(journalLine);
    }

    /**
     * Partially update a journalLine.
     *
     * @param journalLine the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JournalLine> partialUpdate(JournalLine journalLine) {
        LOG.debug("Request to partially update JournalLine : {}", journalLine);

        return journalLineRepository
            .findById(journalLine.getId())
            .map(existingJournalLine -> {
                if (journalLine.getJournalId() != null) {
                    existingJournalLine.setJournalId(journalLine.getJournalId());
                }
                if (journalLine.getAccountId() != null) {
                    existingJournalLine.setAccountId(journalLine.getAccountId());
                }
                if (journalLine.getDebit() != null) {
                    existingJournalLine.setDebit(journalLine.getDebit());
                }
                if (journalLine.getCredit() != null) {
                    existingJournalLine.setCredit(journalLine.getCredit());
                }

                return existingJournalLine;
            })
            .map(journalLineRepository::save);
    }

    /**
     * Get all the journalLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JournalLine> findAll(Pageable pageable) {
        LOG.debug("Request to get all JournalLines");
        return journalLineRepository.findAll(pageable);
    }

    /**
     * Get one journalLine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JournalLine> findOne(Long id) {
        LOG.debug("Request to get JournalLine : {}", id);
        return journalLineRepository.findById(id);
    }

    /**
     * Delete the journalLine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete JournalLine : {}", id);
        journalLineRepository.deleteById(id);
    }
}
