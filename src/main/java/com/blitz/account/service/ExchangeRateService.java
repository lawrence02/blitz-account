package com.blitz.account.service;

import com.blitz.account.domain.ExchangeRate;
import com.blitz.account.repository.ExchangeRateRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.ExchangeRate}.
 */
@Service
@Transactional
public class ExchangeRateService {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateService.class);

    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    /**
     * Save a exchangeRate.
     *
     * @param exchangeRate the entity to save.
     * @return the persisted entity.
     */
    public ExchangeRate save(ExchangeRate exchangeRate) {
        LOG.debug("Request to save ExchangeRate : {}", exchangeRate);
        return exchangeRateRepository.save(exchangeRate);
    }

    /**
     * Update a exchangeRate.
     *
     * @param exchangeRate the entity to save.
     * @return the persisted entity.
     */
    public ExchangeRate update(ExchangeRate exchangeRate) {
        LOG.debug("Request to update ExchangeRate : {}", exchangeRate);
        return exchangeRateRepository.save(exchangeRate);
    }

    /**
     * Partially update a exchangeRate.
     *
     * @param exchangeRate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExchangeRate> partialUpdate(ExchangeRate exchangeRate) {
        LOG.debug("Request to partially update ExchangeRate : {}", exchangeRate);

        return exchangeRateRepository
            .findById(exchangeRate.getId())
            .map(existingExchangeRate -> {
                if (exchangeRate.getBaseCurrencyId() != null) {
                    existingExchangeRate.setBaseCurrencyId(exchangeRate.getBaseCurrencyId());
                }
                if (exchangeRate.getTargetCurrencyId() != null) {
                    existingExchangeRate.setTargetCurrencyId(exchangeRate.getTargetCurrencyId());
                }
                if (exchangeRate.getRate() != null) {
                    existingExchangeRate.setRate(exchangeRate.getRate());
                }
                if (exchangeRate.getRateDate() != null) {
                    existingExchangeRate.setRateDate(exchangeRate.getRateDate());
                }

                return existingExchangeRate;
            })
            .map(exchangeRateRepository::save);
    }

    /**
     * Get all the exchangeRates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExchangeRate> findAll(Pageable pageable) {
        LOG.debug("Request to get all ExchangeRates");
        return exchangeRateRepository.findAll(pageable);
    }

    /**
     * Get one exchangeRate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExchangeRate> findOne(Long id) {
        LOG.debug("Request to get ExchangeRate : {}", id);
        return exchangeRateRepository.findById(id);
    }

    /**
     * Delete the exchangeRate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExchangeRate : {}", id);
        exchangeRateRepository.deleteById(id);
    }
}
