package com.blitz.account.web.rest;

import static com.blitz.account.domain.RecurringTransactionAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.RecurringTransaction;
import com.blitz.account.repository.RecurringTransactionRepository;
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
 * Integration tests for the {@link RecurringTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecurringTransactionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ACCOUNT_ID = 1L;
    private static final Long UPDATED_ACCOUNT_ID = 2L;

    private static final Long DEFAULT_CURRENCY_ID = 1L;
    private static final Long UPDATED_CURRENCY_ID = 2L;

    private static final Long DEFAULT_VAT_RATE_ID = 1L;
    private static final Long UPDATED_VAT_RATE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/recurring-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecurringTransactionRepository recurringTransactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecurringTransactionMockMvc;

    private RecurringTransaction recurringTransaction;

    private RecurringTransaction insertedRecurringTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecurringTransaction createEntity() {
        return new RecurringTransaction()
            .name(DEFAULT_NAME)
            .amount(DEFAULT_AMOUNT)
            .frequency(DEFAULT_FREQUENCY)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .accountId(DEFAULT_ACCOUNT_ID)
            .currencyId(DEFAULT_CURRENCY_ID)
            .vatRateId(DEFAULT_VAT_RATE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecurringTransaction createUpdatedEntity() {
        return new RecurringTransaction()
            .name(UPDATED_NAME)
            .amount(UPDATED_AMOUNT)
            .frequency(UPDATED_FREQUENCY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .accountId(UPDATED_ACCOUNT_ID)
            .currencyId(UPDATED_CURRENCY_ID)
            .vatRateId(UPDATED_VAT_RATE_ID);
    }

    @BeforeEach
    void initTest() {
        recurringTransaction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecurringTransaction != null) {
            recurringTransactionRepository.delete(insertedRecurringTransaction);
            insertedRecurringTransaction = null;
        }
    }

    @Test
    @Transactional
    void createRecurringTransaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RecurringTransaction
        var returnedRecurringTransaction = om.readValue(
            restRecurringTransactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recurringTransaction)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecurringTransaction.class
        );

        // Validate the RecurringTransaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRecurringTransactionUpdatableFieldsEquals(
            returnedRecurringTransaction,
            getPersistedRecurringTransaction(returnedRecurringTransaction)
        );

        insertedRecurringTransaction = returnedRecurringTransaction;
    }

    @Test
    @Transactional
    void createRecurringTransactionWithExistingId() throws Exception {
        // Create the RecurringTransaction with an existing ID
        recurringTransaction.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecurringTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recurringTransaction)))
            .andExpect(status().isBadRequest());

        // Validate the RecurringTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recurringTransaction.setName(null);

        // Create the RecurringTransaction, which fails.

        restRecurringTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recurringTransaction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recurringTransaction.setAmount(null);

        // Create the RecurringTransaction, which fails.

        restRecurringTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recurringTransaction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFrequencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recurringTransaction.setFrequency(null);

        // Create the RecurringTransaction, which fails.

        restRecurringTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recurringTransaction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recurringTransaction.setStartDate(null);

        // Create the RecurringTransaction, which fails.

        restRecurringTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recurringTransaction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecurringTransactions() throws Exception {
        // Initialize the database
        insertedRecurringTransaction = recurringTransactionRepository.saveAndFlush(recurringTransaction);

        // Get all the recurringTransactionList
        restRecurringTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recurringTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].currencyId").value(hasItem(DEFAULT_CURRENCY_ID.intValue())))
            .andExpect(jsonPath("$.[*].vatRateId").value(hasItem(DEFAULT_VAT_RATE_ID.intValue())));
    }

    @Test
    @Transactional
    void getRecurringTransaction() throws Exception {
        // Initialize the database
        insertedRecurringTransaction = recurringTransactionRepository.saveAndFlush(recurringTransaction);

        // Get the recurringTransaction
        restRecurringTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, recurringTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recurringTransaction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.accountId").value(DEFAULT_ACCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.currencyId").value(DEFAULT_CURRENCY_ID.intValue()))
            .andExpect(jsonPath("$.vatRateId").value(DEFAULT_VAT_RATE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRecurringTransaction() throws Exception {
        // Get the recurringTransaction
        restRecurringTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecurringTransaction() throws Exception {
        // Initialize the database
        insertedRecurringTransaction = recurringTransactionRepository.saveAndFlush(recurringTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recurringTransaction
        RecurringTransaction updatedRecurringTransaction = recurringTransactionRepository
            .findById(recurringTransaction.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRecurringTransaction are not directly saved in db
        em.detach(updatedRecurringTransaction);
        updatedRecurringTransaction
            .name(UPDATED_NAME)
            .amount(UPDATED_AMOUNT)
            .frequency(UPDATED_FREQUENCY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .accountId(UPDATED_ACCOUNT_ID)
            .currencyId(UPDATED_CURRENCY_ID)
            .vatRateId(UPDATED_VAT_RATE_ID);

        restRecurringTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecurringTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRecurringTransaction))
            )
            .andExpect(status().isOk());

        // Validate the RecurringTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecurringTransactionToMatchAllProperties(updatedRecurringTransaction);
    }

    @Test
    @Transactional
    void putNonExistingRecurringTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recurringTransaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecurringTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recurringTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recurringTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecurringTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecurringTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recurringTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecurringTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recurringTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecurringTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecurringTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recurringTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecurringTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recurringTransaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecurringTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecurringTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedRecurringTransaction = recurringTransactionRepository.saveAndFlush(recurringTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recurringTransaction using partial update
        RecurringTransaction partialUpdatedRecurringTransaction = new RecurringTransaction();
        partialUpdatedRecurringTransaction.setId(recurringTransaction.getId());

        partialUpdatedRecurringTransaction
            .name(UPDATED_NAME)
            .frequency(UPDATED_FREQUENCY)
            .endDate(UPDATED_END_DATE)
            .currencyId(UPDATED_CURRENCY_ID)
            .vatRateId(UPDATED_VAT_RATE_ID);

        restRecurringTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecurringTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecurringTransaction))
            )
            .andExpect(status().isOk());

        // Validate the RecurringTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecurringTransactionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecurringTransaction, recurringTransaction),
            getPersistedRecurringTransaction(recurringTransaction)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecurringTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedRecurringTransaction = recurringTransactionRepository.saveAndFlush(recurringTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recurringTransaction using partial update
        RecurringTransaction partialUpdatedRecurringTransaction = new RecurringTransaction();
        partialUpdatedRecurringTransaction.setId(recurringTransaction.getId());

        partialUpdatedRecurringTransaction
            .name(UPDATED_NAME)
            .amount(UPDATED_AMOUNT)
            .frequency(UPDATED_FREQUENCY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .accountId(UPDATED_ACCOUNT_ID)
            .currencyId(UPDATED_CURRENCY_ID)
            .vatRateId(UPDATED_VAT_RATE_ID);

        restRecurringTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecurringTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecurringTransaction))
            )
            .andExpect(status().isOk());

        // Validate the RecurringTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecurringTransactionUpdatableFieldsEquals(
            partialUpdatedRecurringTransaction,
            getPersistedRecurringTransaction(partialUpdatedRecurringTransaction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRecurringTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recurringTransaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecurringTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recurringTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recurringTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecurringTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecurringTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recurringTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecurringTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recurringTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecurringTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecurringTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recurringTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecurringTransactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recurringTransaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecurringTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecurringTransaction() throws Exception {
        // Initialize the database
        insertedRecurringTransaction = recurringTransactionRepository.saveAndFlush(recurringTransaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recurringTransaction
        restRecurringTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, recurringTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recurringTransactionRepository.count();
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

    protected RecurringTransaction getPersistedRecurringTransaction(RecurringTransaction recurringTransaction) {
        return recurringTransactionRepository.findById(recurringTransaction.getId()).orElseThrow();
    }

    protected void assertPersistedRecurringTransactionToMatchAllProperties(RecurringTransaction expectedRecurringTransaction) {
        assertRecurringTransactionAllPropertiesEquals(
            expectedRecurringTransaction,
            getPersistedRecurringTransaction(expectedRecurringTransaction)
        );
    }

    protected void assertPersistedRecurringTransactionToMatchUpdatableProperties(RecurringTransaction expectedRecurringTransaction) {
        assertRecurringTransactionAllUpdatablePropertiesEquals(
            expectedRecurringTransaction,
            getPersistedRecurringTransaction(expectedRecurringTransaction)
        );
    }
}
