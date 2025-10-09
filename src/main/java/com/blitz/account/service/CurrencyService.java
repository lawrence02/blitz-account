package com.blitz.account.service;

import com.blitz.account.domain.Currency;
import com.blitz.account.repository.CurrencyRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.Currency}.
 */
@Service
@Transactional
public class CurrencyService {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * Save a currency.
     *
     * @param currency the entity to save.
     * @return the persisted entity.
     */
    public Currency save(Currency currency) {
        LOG.debug("Request to save Currency : {}", currency);
        return currencyRepository.save(currency);
    }

    /**
     * Update a currency.
     *
     * @param currency the entity to save.
     * @return the persisted entity.
     */
    public Currency update(Currency currency) {
        LOG.debug("Request to update Currency : {}", currency);
        return currencyRepository.save(currency);
    }

    /**
     * Partially update a currency.
     *
     * @param currency the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Currency> partialUpdate(Currency currency) {
        LOG.debug("Request to partially update Currency : {}", currency);

        return currencyRepository
            .findById(currency.getId())
            .map(existingCurrency -> {
                if (currency.getCode() != null) {
                    existingCurrency.setCode(currency.getCode());
                }
                if (currency.getName() != null) {
                    existingCurrency.setName(currency.getName());
                }
                if (currency.getSymbol() != null) {
                    existingCurrency.setSymbol(currency.getSymbol());
                }

                return existingCurrency;
            })
            .map(currencyRepository::save);
    }

    /**
     * Get all the currencies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Currency> findAll(Pageable pageable) {
        LOG.debug("Request to get all Currencies");
        return currencyRepository.findAll(pageable);
    }

    /**
     * Get one currency by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Currency> findOne(Long id) {
        LOG.debug("Request to get Currency : {}", id);
        return currencyRepository.findById(id);
    }

    /**
     * Delete the currency by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Currency : {}", id);
        currencyRepository.deleteById(id);
    }
}
