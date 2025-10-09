package com.blitz.account.web.rest;

import static com.blitz.account.domain.QuoteLineAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.QuoteLine;
import com.blitz.account.repository.QuoteLineRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link QuoteLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuoteLineResourceIT {

    private static final Long DEFAULT_QUOTE_ID = 1L;
    private static final Long UPDATED_QUOTE_ID = 2L;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);

    private static final Long DEFAULT_VAT_RATE_ID = 1L;
    private static final Long UPDATED_VAT_RATE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/quote-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuoteLineRepository quoteLineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuoteLineMockMvc;

    private QuoteLine quoteLine;

    private QuoteLine insertedQuoteLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteLine createEntity() {
        return new QuoteLine()
            .quoteId(DEFAULT_QUOTE_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .vatRateId(DEFAULT_VAT_RATE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuoteLine createUpdatedEntity() {
        return new QuoteLine()
            .quoteId(UPDATED_QUOTE_ID)
            .productId(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRateId(UPDATED_VAT_RATE_ID);
    }

    @BeforeEach
    void initTest() {
        quoteLine = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuoteLine != null) {
            quoteLineRepository.delete(insertedQuoteLine);
            insertedQuoteLine = null;
        }
    }

    @Test
    @Transactional
    void createQuoteLine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuoteLine
        var returnedQuoteLine = om.readValue(
            restQuoteLineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteLine)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuoteLine.class
        );

        // Validate the QuoteLine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuoteLineUpdatableFieldsEquals(returnedQuoteLine, getPersistedQuoteLine(returnedQuoteLine));

        insertedQuoteLine = returnedQuoteLine;
    }

    @Test
    @Transactional
    void createQuoteLineWithExistingId() throws Exception {
        // Create the QuoteLine with an existing ID
        quoteLine.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteLine)))
            .andExpect(status().isBadRequest());

        // Validate the QuoteLine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuoteIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quoteLine.setQuoteId(null);

        // Create the QuoteLine, which fails.

        restQuoteLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteLine)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuoteLines() throws Exception {
        // Initialize the database
        insertedQuoteLine = quoteLineRepository.saveAndFlush(quoteLine);

        // Get all the quoteLineList
        restQuoteLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quoteLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].quoteId").value(hasItem(DEFAULT_QUOTE_ID.intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].vatRateId").value(hasItem(DEFAULT_VAT_RATE_ID.intValue())));
    }

    @Test
    @Transactional
    void getQuoteLine() throws Exception {
        // Initialize the database
        insertedQuoteLine = quoteLineRepository.saveAndFlush(quoteLine);

        // Get the quoteLine
        restQuoteLineMockMvc
            .perform(get(ENTITY_API_URL_ID, quoteLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quoteLine.getId().intValue()))
            .andExpect(jsonPath("$.quoteId").value(DEFAULT_QUOTE_ID.intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.vatRateId").value(DEFAULT_VAT_RATE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuoteLine() throws Exception {
        // Get the quoteLine
        restQuoteLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuoteLine() throws Exception {
        // Initialize the database
        insertedQuoteLine = quoteLineRepository.saveAndFlush(quoteLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteLine
        QuoteLine updatedQuoteLine = quoteLineRepository.findById(quoteLine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuoteLine are not directly saved in db
        em.detach(updatedQuoteLine);
        updatedQuoteLine
            .quoteId(UPDATED_QUOTE_ID)
            .productId(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRateId(UPDATED_VAT_RATE_ID);

        restQuoteLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuoteLine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuoteLine))
            )
            .andExpect(status().isOk());

        // Validate the QuoteLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuoteLineToMatchAllProperties(updatedQuoteLine);
    }

    @Test
    @Transactional
    void putNonExistingQuoteLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quoteLine.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuoteLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuoteLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuoteLineWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteLine = quoteLineRepository.saveAndFlush(quoteLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteLine using partial update
        QuoteLine partialUpdatedQuoteLine = new QuoteLine();
        partialUpdatedQuoteLine.setId(quoteLine.getId());

        partialUpdatedQuoteLine
            .quoteId(UPDATED_QUOTE_ID)
            .productId(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE);

        restQuoteLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteLine))
            )
            .andExpect(status().isOk());

        // Validate the QuoteLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteLineUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuoteLine, quoteLine),
            getPersistedQuoteLine(quoteLine)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuoteLineWithPatch() throws Exception {
        // Initialize the database
        insertedQuoteLine = quoteLineRepository.saveAndFlush(quoteLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quoteLine using partial update
        QuoteLine partialUpdatedQuoteLine = new QuoteLine();
        partialUpdatedQuoteLine.setId(quoteLine.getId());

        partialUpdatedQuoteLine
            .quoteId(UPDATED_QUOTE_ID)
            .productId(UPDATED_PRODUCT_ID)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .vatRateId(UPDATED_VAT_RATE_ID);

        restQuoteLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuoteLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuoteLine))
            )
            .andExpect(status().isOk());

        // Validate the QuoteLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteLineUpdatableFieldsEquals(partialUpdatedQuoteLine, getPersistedQuoteLine(partialUpdatedQuoteLine));
    }

    @Test
    @Transactional
    void patchNonExistingQuoteLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quoteLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuoteLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuoteLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuoteLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteLineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quoteLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuoteLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuoteLine() throws Exception {
        // Initialize the database
        insertedQuoteLine = quoteLineRepository.saveAndFlush(quoteLine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quoteLine
        restQuoteLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, quoteLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quoteLineRepository.count();
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

    protected QuoteLine getPersistedQuoteLine(QuoteLine quoteLine) {
        return quoteLineRepository.findById(quoteLine.getId()).orElseThrow();
    }

    protected void assertPersistedQuoteLineToMatchAllProperties(QuoteLine expectedQuoteLine) {
        assertQuoteLineAllPropertiesEquals(expectedQuoteLine, getPersistedQuoteLine(expectedQuoteLine));
    }

    protected void assertPersistedQuoteLineToMatchUpdatableProperties(QuoteLine expectedQuoteLine) {
        assertQuoteLineAllUpdatablePropertiesEquals(expectedQuoteLine, getPersistedQuoteLine(expectedQuoteLine));
    }
}
