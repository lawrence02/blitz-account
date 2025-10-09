package com.blitz.account.service;

import com.blitz.account.domain.QuoteLine;
import com.blitz.account.repository.QuoteLineRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.QuoteLine}.
 */
@Service
@Transactional
public class QuoteLineService {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteLineService.class);

    private final QuoteLineRepository quoteLineRepository;

    public QuoteLineService(QuoteLineRepository quoteLineRepository) {
        this.quoteLineRepository = quoteLineRepository;
    }

    /**
     * Save a quoteLine.
     *
     * @param quoteLine the entity to save.
     * @return the persisted entity.
     */
    public QuoteLine save(QuoteLine quoteLine) {
        LOG.debug("Request to save QuoteLine : {}", quoteLine);
        return quoteLineRepository.save(quoteLine);
    }

    /**
     * Update a quoteLine.
     *
     * @param quoteLine the entity to save.
     * @return the persisted entity.
     */
    public QuoteLine update(QuoteLine quoteLine) {
        LOG.debug("Request to update QuoteLine : {}", quoteLine);
        return quoteLineRepository.save(quoteLine);
    }

    /**
     * Partially update a quoteLine.
     *
     * @param quoteLine the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuoteLine> partialUpdate(QuoteLine quoteLine) {
        LOG.debug("Request to partially update QuoteLine : {}", quoteLine);

        return quoteLineRepository
            .findById(quoteLine.getId())
            .map(existingQuoteLine -> {
                if (quoteLine.getQuoteId() != null) {
                    existingQuoteLine.setQuoteId(quoteLine.getQuoteId());
                }
                if (quoteLine.getProductId() != null) {
                    existingQuoteLine.setProductId(quoteLine.getProductId());
                }
                if (quoteLine.getQuantity() != null) {
                    existingQuoteLine.setQuantity(quoteLine.getQuantity());
                }
                if (quoteLine.getUnitPrice() != null) {
                    existingQuoteLine.setUnitPrice(quoteLine.getUnitPrice());
                }
                if (quoteLine.getVatRateId() != null) {
                    existingQuoteLine.setVatRateId(quoteLine.getVatRateId());
                }

                return existingQuoteLine;
            })
            .map(quoteLineRepository::save);
    }

    /**
     * Get all the quoteLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuoteLine> findAll(Pageable pageable) {
        LOG.debug("Request to get all QuoteLines");
        return quoteLineRepository.findAll(pageable);
    }

    /**
     * Get one quoteLine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuoteLine> findOne(Long id) {
        LOG.debug("Request to get QuoteLine : {}", id);
        return quoteLineRepository.findById(id);
    }

    /**
     * Delete the quoteLine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete QuoteLine : {}", id);
        quoteLineRepository.deleteById(id);
    }
}
