package com.blitz.account.web.rest;

import static com.blitz.account.domain.CashSaleAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.CashSale;
import com.blitz.account.repository.CashSaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CashSaleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CashSaleResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);

    private static final Long DEFAULT_VAT_RATE_ID = 1L;
    private static final Long UPDATED_VAT_RATE_ID = 2L;

    private static final Long DEFAULT_CURRENCY_ID = 1L;
    private static final Long UPDATED_CURRENCY_ID = 2L;

    private static final Instant DEFAULT_SALE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SALE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/cash-sales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CashSaleRepository cashSaleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCashSaleMockMvc;

    private CashSale cashSale;

    private CashSale insertedCashSale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashSale createEntity() {
        return new CashSale()
            .productId(DEFAULT_PRODUCT_ID)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .vatRateId(DEFAULT_VAT_RATE_ID)
            .currencyId(DEFAULT_CURRENCY_ID)
            .saleDate(DEFAULT_SALE_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashSale createUpdatedEntity() {
        return new CashSale()
            .productId(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRateId(UPDATED_VAT_RATE_ID)
            .currencyId(UPDATED_CURRENCY_ID)
            .saleDate(UPDATED_SALE_DATE);
    }

    @BeforeEach
    void initTest() {
        cashSale = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCashSale != null) {
            cashSaleRepository.delete(insertedCashSale);
            insertedCashSale = null;
        }
    }

    @Test
    @Transactional
    void createCashSale() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CashSale
        var returnedCashSale = om.readValue(
            restCashSaleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashSale)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CashSale.class
        );

        // Validate the CashSale in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCashSaleUpdatableFieldsEquals(returnedCashSale, getPersistedCashSale(returnedCashSale));

        insertedCashSale = returnedCashSale;
    }

    @Test
    @Transactional
    void createCashSaleWithExistingId() throws Exception {
        // Create the CashSale with an existing ID
        cashSale.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashSale)))
            .andExpect(status().isBadRequest());

        // Validate the CashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCashSales() throws Exception {
        // Initialize the database
        insertedCashSale = cashSaleRepository.saveAndFlush(cashSale);

        // Get all the cashSaleList
        restCashSaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashSale.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].vatRateId").value(hasItem(DEFAULT_VAT_RATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].currencyId").value(hasItem(DEFAULT_CURRENCY_ID.intValue())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())));
    }

    @Test
    @Transactional
    void getCashSale() throws Exception {
        // Initialize the database
        insertedCashSale = cashSaleRepository.saveAndFlush(cashSale);

        // Get the cashSale
        restCashSaleMockMvc
            .perform(get(ENTITY_API_URL_ID, cashSale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cashSale.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.vatRateId").value(DEFAULT_VAT_RATE_ID.intValue()))
            .andExpect(jsonPath("$.currencyId").value(DEFAULT_CURRENCY_ID.intValue()))
            .andExpect(jsonPath("$.saleDate").value(DEFAULT_SALE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCashSale() throws Exception {
        // Get the cashSale
        restCashSaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCashSale() throws Exception {
        // Initialize the database
        insertedCashSale = cashSaleRepository.saveAndFlush(cashSale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cashSale
        CashSale updatedCashSale = cashSaleRepository.findById(cashSale.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCashSale are not directly saved in db
        em.detach(updatedCashSale);
        updatedCashSale
            .productId(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRateId(UPDATED_VAT_RATE_ID)
            .currencyId(UPDATED_CURRENCY_ID)
            .saleDate(UPDATED_SALE_DATE);

        restCashSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCashSale.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCashSale))
            )
            .andExpect(status().isOk());

        // Validate the CashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCashSaleToMatchAllProperties(updatedCashSale);
    }

    @Test
    @Transactional
    void putNonExistingCashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashSale.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cashSale.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashSale))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashSale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cashSale))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashSale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashSaleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cashSale)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCashSaleWithPatch() throws Exception {
        // Initialize the database
        insertedCashSale = cashSaleRepository.saveAndFlush(cashSale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cashSale using partial update
        CashSale partialUpdatedCashSale = new CashSale();
        partialUpdatedCashSale.setId(cashSale.getId());

        partialUpdatedCashSale
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRateId(UPDATED_VAT_RATE_ID)
            .currencyId(UPDATED_CURRENCY_ID)
            .saleDate(UPDATED_SALE_DATE);

        restCashSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashSale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCashSale))
            )
            .andExpect(status().isOk());

        // Validate the CashSale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCashSaleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCashSale, cashSale), getPersistedCashSale(cashSale));
    }

    @Test
    @Transactional
    void fullUpdateCashSaleWithPatch() throws Exception {
        // Initialize the database
        insertedCashSale = cashSaleRepository.saveAndFlush(cashSale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cashSale using partial update
        CashSale partialUpdatedCashSale = new CashSale();
        partialUpdatedCashSale.setId(cashSale.getId());

        partialUpdatedCashSale
            .productId(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRateId(UPDATED_VAT_RATE_ID)
            .currencyId(UPDATED_CURRENCY_ID)
            .saleDate(UPDATED_SALE_DATE);

        restCashSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashSale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCashSale))
            )
            .andExpect(status().isOk());

        // Validate the CashSale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCashSaleUpdatableFieldsEquals(partialUpdatedCashSale, getPersistedCashSale(partialUpdatedCashSale));
    }

    @Test
    @Transactional
    void patchNonExistingCashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashSale.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cashSale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cashSale))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashSale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cashSale))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cashSale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashSaleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cashSale)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCashSale() throws Exception {
        // Initialize the database
        insertedCashSale = cashSaleRepository.saveAndFlush(cashSale);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cashSale
        restCashSaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, cashSale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cashSaleRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CashSale getPersistedCashSale(CashSale cashSale) {
        return cashSaleRepository.findById(cashSale.getId()).orElseThrow();
    }

    protected void assertPersistedCashSaleToMatchAllProperties(CashSale expectedCashSale) {
        assertCashSaleAllPropertiesEquals(expectedCashSale, getPersistedCashSale(expectedCashSale));
    }

    protected void assertPersistedCashSaleToMatchUpdatableProperties(CashSale expectedCashSale) {
        assertCashSaleAllUpdatablePropertiesEquals(expectedCashSale, getPersistedCashSale(expectedCashSale));
    }
}
