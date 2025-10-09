package com.blitz.account.web.rest;

import static com.blitz.account.domain.QuoteAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.Quote;
import com.blitz.account.domain.enumeration.DocumentStatus;
import com.blitz.account.repository.QuoteRepository;
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
 * Integration tests for the {@link QuoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuoteResourceIT {

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DocumentStatus DEFAULT_STATUS = DocumentStatus.DRAFT;
    private static final DocumentStatus UPDATED_STATUS = DocumentStatus.SENT;

    private static final Long DEFAULT_CURRENCY_ID = 1L;
    private static final Long UPDATED_CURRENCY_ID = 2L;

    private static final Long DEFAULT_VAT_RATE_ID = 1L;
    private static final Long UPDATED_VAT_RATE_ID = 2L;

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/quotes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuoteMockMvc;

    private Quote quote;

    private Quote insertedQuote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quote createEntity() {
        return new Quote()
            .clientName(DEFAULT_CLIENT_NAME)
            .issueDate(DEFAULT_ISSUE_DATE)
            .status(DEFAULT_STATUS)
            .currencyId(DEFAULT_CURRENCY_ID)
            .vatRateId(DEFAULT_VAT_RATE_ID)
            .totalAmount(DEFAULT_TOTAL_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quote createUpdatedEntity() {
        return new Quote()
            .clientName(UPDATED_CLIENT_NAME)
            .issueDate(UPDATED_ISSUE_DATE)
            .status(UPDATED_STATUS)
            .currencyId(UPDATED_CURRENCY_ID)
            .vatRateId(UPDATED_VAT_RATE_ID)
            .totalAmount(UPDATED_TOTAL_AMOUNT);
    }

    @BeforeEach
    void initTest() {
        quote = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuote != null) {
            quoteRepository.delete(insertedQuote);
            insertedQuote = null;
        }
    }

    @Test
    @Transactional
    void createQuote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Quote
        var returnedQuote = om.readValue(
            restQuoteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Quote.class
        );

        // Validate the Quote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuoteUpdatableFieldsEquals(returnedQuote, getPersistedQuote(returnedQuote));

        insertedQuote = returnedQuote;
    }

    @Test
    @Transactional
    void createQuoteWithExistingId() throws Exception {
        // Create the Quote with an existing ID
        quote.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClientNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quote.setClientName(null);

        // Create the Quote, which fails.

        restQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quote.setIssueDate(null);

        // Create the Quote, which fails.

        restQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quote.setStatus(null);

        // Create the Quote, which fails.

        restQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuotes() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quote.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].currencyId").value(hasItem(DEFAULT_CURRENCY_ID.intValue())))
            .andExpect(jsonPath("$.[*].vatRateId").value(hasItem(DEFAULT_VAT_RATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))));
    }

    @Test
    @Transactional
    void getQuote() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get the quote
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL_ID, quote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quote.getId().intValue()))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.currencyId").value(DEFAULT_CURRENCY_ID.intValue()))
            .andExpect(jsonPath("$.vatRateId").value(DEFAULT_VAT_RATE_ID.intValue()))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingQuote() throws Exception {
        // Get the quote
        restQuoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuote() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quote
        Quote updatedQuote = quoteRepository.findById(quote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuote are not directly saved in db
        em.detach(updatedQuote);
        updatedQuote
            .clientName(UPDATED_CLIENT_NAME)
            .issueDate(UPDATED_ISSUE_DATE)
            .status(UPDATED_STATUS)
            .currencyId(UPDATED_CURRENCY_ID)
            .vatRateId(UPDATED_VAT_RATE_ID)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuote))
            )
            .andExpect(status().isOk());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuoteToMatchAllProperties(updatedQuote);
    }

    @Test
    @Transactional
    void putNonExistingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(put(ENTITY_API_URL_ID, quote.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuoteWithPatch() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quote using partial update
        Quote partialUpdatedQuote = new Quote();
        partialUpdatedQuote.setId(quote.getId());

        partialUpdatedQuote
            .clientName(UPDATED_CLIENT_NAME)
            .issueDate(UPDATED_ISSUE_DATE)
            .status(UPDATED_STATUS)
            .currencyId(UPDATED_CURRENCY_ID)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuote))
            )
            .andExpect(status().isOk());

        // Validate the Quote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuote, quote), getPersistedQuote(quote));
    }

    @Test
    @Transactional
    void fullUpdateQuoteWithPatch() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quote using partial update
        Quote partialUpdatedQuote = new Quote();
        partialUpdatedQuote.setId(quote.getId());

        partialUpdatedQuote
            .clientName(UPDATED_CLIENT_NAME)
            .issueDate(UPDATED_ISSUE_DATE)
            .status(UPDATED_STATUS)
            .currencyId(UPDATED_CURRENCY_ID)
            .vatRateId(UPDATED_VAT_RATE_ID)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuote))
            )
            .andExpect(status().isOk());

        // Validate the Quote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteUpdatableFieldsEquals(partialUpdatedQuote, getPersistedQuote(partialUpdatedQuote));
    }

    @Test
    @Transactional
    void patchNonExistingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quote.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuote() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quote
        restQuoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, quote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quoteRepository.count();
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

    protected Quote getPersistedQuote(Quote quote) {
        return quoteRepository.findById(quote.getId()).orElseThrow();
    }

    protected void assertPersistedQuoteToMatchAllProperties(Quote expectedQuote) {
        assertQuoteAllPropertiesEquals(expectedQuote, getPersistedQuote(expectedQuote));
    }

    protected void assertPersistedQuoteToMatchUpdatableProperties(Quote expectedQuote) {
        assertQuoteAllUpdatablePropertiesEquals(expectedQuote, getPersistedQuote(expectedQuote));
    }
}
