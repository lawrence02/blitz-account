package com.blitz.account.service;

import com.blitz.account.domain.Quote;
import com.blitz.account.repository.QuoteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.Quote}.
 */
@Service
@Transactional
public class QuoteService {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteService.class);

    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    /**
     * Save a quote.
     *
     * @param quote the entity to save.
     * @return the persisted entity.
     */
    public Quote save(Quote quote) {
        LOG.debug("Request to save Quote : {}", quote);
        return quoteRepository.save(quote);
    }

    /**
     * Update a quote.
     *
     * @param quote the entity to save.
     * @return the persisted entity.
     */
    public Quote update(Quote quote) {
        LOG.debug("Request to update Quote : {}", quote);
        return quoteRepository.save(quote);
    }

    /**
     * Partially update a quote.
     *
     * @param quote the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Quote> partialUpdate(Quote quote) {
        LOG.debug("Request to partially update Quote : {}", quote);

        return quoteRepository
            .findById(quote.getId())
            .map(existingQuote -> {
                if (quote.getClientName() != null) {
                    existingQuote.setClientName(quote.getClientName());
                }
                if (quote.getIssueDate() != null) {
                    existingQuote.setIssueDate(quote.getIssueDate());
                }
                if (quote.getStatus() != null) {
                    existingQuote.setStatus(quote.getStatus());
                }
                if (quote.getCurrencyId() != null) {
                    existingQuote.setCurrencyId(quote.getCurrencyId());
                }
                if (quote.getVatRateId() != null) {
                    existingQuote.setVatRateId(quote.getVatRateId());
                }
                if (quote.getTotalAmount() != null) {
                    existingQuote.setTotalAmount(quote.getTotalAmount());
                }

                return existingQuote;
            })
            .map(quoteRepository::save);
    }

    /**
     * Get all the quotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Quote> findAll(Pageable pageable) {
        LOG.debug("Request to get all Quotes");
        return quoteRepository.findAll(pageable);
    }

    /**
     * Get one quote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Quote> findOne(Long id) {
        LOG.debug("Request to get Quote : {}", id);
        return quoteRepository.findById(id);
    }

    /**
     * Delete the quote by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Quote : {}", id);
        quoteRepository.deleteById(id);
    }
}
