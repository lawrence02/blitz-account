package com.blitz.account.service;

import com.blitz.account.domain.InvoiceLine;
import com.blitz.account.repository.InvoiceLineRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blitz.account.domain.InvoiceLine}.
 */
@Service
@Transactional
public class InvoiceLineService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceLineService.class);

    private final InvoiceLineRepository invoiceLineRepository;

    public InvoiceLineService(InvoiceLineRepository invoiceLineRepository) {
        this.invoiceLineRepository = invoiceLineRepository;
    }

    /**
     * Save a invoiceLine.
     *
     * @param invoiceLine the entity to save.
     * @return the persisted entity.
     */
    public InvoiceLine save(InvoiceLine invoiceLine) {
        LOG.debug("Request to save InvoiceLine : {}", invoiceLine);
        return invoiceLineRepository.save(invoiceLine);
    }

    /**
     * Update a invoiceLine.
     *
     * @param invoiceLine the entity to save.
     * @return the persisted entity.
     */
    public InvoiceLine update(InvoiceLine invoiceLine) {
        LOG.debug("Request to update InvoiceLine : {}", invoiceLine);
        return invoiceLineRepository.save(invoiceLine);
    }

    /**
     * Partially update a invoiceLine.
     *
     * @param invoiceLine the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceLine> partialUpdate(InvoiceLine invoiceLine) {
        LOG.debug("Request to partially update InvoiceLine : {}", invoiceLine);

        return invoiceLineRepository
            .findById(invoiceLine.getId())
            .map(existingInvoiceLine -> {
                if (invoiceLine.getInvoiceId() != null) {
                    existingInvoiceLine.setInvoiceId(invoiceLine.getInvoiceId());
                }
                if (invoiceLine.getProductId() != null) {
                    existingInvoiceLine.setProductId(invoiceLine.getProductId());
                }
                if (invoiceLine.getQuantity() != null) {
                    existingInvoiceLine.setQuantity(invoiceLine.getQuantity());
                }
                if (invoiceLine.getUnitPrice() != null) {
                    existingInvoiceLine.setUnitPrice(invoiceLine.getUnitPrice());
                }
                if (invoiceLine.getVatRateId() != null) {
                    existingInvoiceLine.setVatRateId(invoiceLine.getVatRateId());
                }

                return existingInvoiceLine;
            })
            .map(invoiceLineRepository::save);
    }

    /**
     * Get all the invoiceLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceLine> findAll(Pageable pageable) {
        LOG.debug("Request to get all InvoiceLines");
        return invoiceLineRepository.findAll(pageable);
    }

    /**
     * Get one invoiceLine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceLine> findOne(Long id) {
        LOG.debug("Request to get InvoiceLine : {}", id);
        return invoiceLineRepository.findById(id);
    }

    /**
     * Delete the invoiceLine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceLine : {}", id);
        invoiceLineRepository.deleteById(id);
    }
}
