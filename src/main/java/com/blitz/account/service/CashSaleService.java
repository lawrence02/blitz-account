package com.blitz.account.service;

import com.blitz.account.domain.CashSale;
import com.blitz.account.repository.CashSaleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.CashSale}.
 */
@Service
@Transactional
public class CashSaleService {

    private static final Logger LOG = LoggerFactory.getLogger(CashSaleService.class);

    private final CashSaleRepository cashSaleRepository;

    public CashSaleService(CashSaleRepository cashSaleRepository) {
        this.cashSaleRepository = cashSaleRepository;
    }

    /**
     * Save a cashSale.
     *
     * @param cashSale the entity to save.
     * @return the persisted entity.
     */
    public CashSale save(CashSale cashSale) {
        LOG.debug("Request to save CashSale : {}", cashSale);
        return cashSaleRepository.save(cashSale);
    }

    /**
     * Update a cashSale.
     *
     * @param cashSale the entity to save.
     * @return the persisted entity.
     */
    public CashSale update(CashSale cashSale) {
        LOG.debug("Request to update CashSale : {}", cashSale);
        return cashSaleRepository.save(cashSale);
    }

    /**
     * Partially update a cashSale.
     *
     * @param cashSale the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CashSale> partialUpdate(CashSale cashSale) {
        LOG.debug("Request to partially update CashSale : {}", cashSale);

        return cashSaleRepository
            .findById(cashSale.getId())
            .map(existingCashSale -> {
                if (cashSale.getProductId() != null) {
                    existingCashSale.setProductId(cashSale.getProductId());
                }
                if (cashSale.getQuantity() != null) {
                    existingCashSale.setQuantity(cashSale.getQuantity());
                }
                if (cashSale.getUnitPrice() != null) {
                    existingCashSale.setUnitPrice(cashSale.getUnitPrice());
                }
                if (cashSale.getVatRateId() != null) {
                    existingCashSale.setVatRateId(cashSale.getVatRateId());
                }
                if (cashSale.getCurrencyId() != null) {
                    existingCashSale.setCurrencyId(cashSale.getCurrencyId());
                }
                if (cashSale.getSaleDate() != null) {
                    existingCashSale.setSaleDate(cashSale.getSaleDate());
                }

                return existingCashSale;
            })
            .map(cashSaleRepository::save);
    }

    /**
     * Get all the cashSales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CashSale> findAll(Pageable pageable) {
        LOG.debug("Request to get all CashSales");
        return cashSaleRepository.findAll(pageable);
    }

    /**
     * Get one cashSale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashSale> findOne(Long id) {
        LOG.debug("Request to get CashSale : {}", id);
        return cashSaleRepository.findById(id);
    }

    /**
     * Delete the cashSale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CashSale : {}", id);
        cashSaleRepository.deleteById(id);
    }
}
