package com.blitz.account.web.rest;

import static com.blitz.account.domain.BankTransactionAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.BankTransaction;
import com.blitz.account.domain.enumeration.TransactionDirection;
import com.blitz.account.repository.BankTransactionRepository;
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
 * Integration tests for the {@link BankTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BankTransactionResourceIT {

    private static final Long DEFAULT_BANK_ACCOUNT_ID = 1L;
    private static final Long UPDATED_BANK_ACCOUNT_ID = 2L;

    private static final Instant DEFAULT_TRANSACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRANSACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final TransactionDirection DEFAULT_DIRECTION = TransactionDirection.CREDIT;
    private static final TransactionDirection UPDATED_DIRECTION = TransactionDirection.DEBIT;

    private static final Long DEFAULT_RELATED_PAYMENT_ID = 1L;
    private static final Long UPDATED_RELATED_PAYMENT_ID = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bank-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankTransactionMockMvc;

    private BankTransaction bankTransaction;

    private BankTransaction insertedBankTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankTransaction createEntity() {
        return new BankTransaction()
            .bankAccountId(DEFAULT_BANK_ACCOUNT_ID)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .reference(DEFAULT_REFERENCE)
            .amount(DEFAULT_AMOUNT)
            .direction(DEFAULT_DIRECTION)
            .relatedPaymentId(DEFAULT_RELATED_PAYMENT_ID)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankTransaction createUpdatedEntity() {
        return new BankTransaction()
            .bankAccountId(UPDATED_BANK_ACCOUNT_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .reference(UPDATED_REFERENCE)
            .amount(UPDATED_AMOUNT)
            .direction(UPDATED_DIRECTION)
            .relatedPaymentId(UPDATED_RELATED_PAYMENT_ID)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        bankTransaction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBankTransaction != null) {
            bankTransactionRepository.delete(insertedBankTransaction);
            insertedBankTransaction = null;
        }
    }

    @Test
    @Transactional
    void createBankTransaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BankTransaction
        var returnedBankTransaction = om.readValue(
            restBankTransactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransaction)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BankTransaction.class
        );

        // Validate the BankTransaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBankTransactionUpdatableFieldsEquals(returnedBankTransaction, getPersistedBankTransaction(returnedBankTransaction));

        insertedBankTransaction = returnedBankTransaction;
    }

    @Test
    @Transactional
    void createBankTransactionWithExistingId() throws Exception {
        // Create the BankTransaction with an existing ID
        bankTransaction.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransaction)))
            .andExpect(status().isBadRequest());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBankAccountIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankTransaction.setBankAccountId(null);

        // Create the BankTransaction, which fails.

        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransaction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankTransaction.setTransactionDate(null);

        // Create the BankTransaction, which fails.

        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransaction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankTransaction.setAmount(null);

        // Create the BankTransaction, which fails.

        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransaction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDirectionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankTransaction.setDirection(null);

        // Create the BankTransaction, which fails.

        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransaction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBankTransactions() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        // Get all the bankTransactionList
        restBankTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankAccountId").value(hasItem(DEFAULT_BANK_ACCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].relatedPaymentId").value(hasItem(DEFAULT_RELATED_PAYMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBankTransaction() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        // Get the bankTransaction
        restBankTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, bankTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankTransaction.getId().intValue()))
            .andExpect(jsonPath("$.bankAccountId").value(DEFAULT_BANK_ACCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()))
            .andExpect(jsonPath("$.relatedPaymentId").value(DEFAULT_RELATED_PAYMENT_ID.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingBankTransaction() throws Exception {
        // Get the bankTransaction
        restBankTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBankTransaction() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankTransaction
        BankTransaction updatedBankTransaction = bankTransactionRepository.findById(bankTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBankTransaction are not directly saved in db
        em.detach(updatedBankTransaction);
        updatedBankTransaction
            .bankAccountId(UPDATED_BANK_ACCOUNT_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .reference(UPDATED_REFERENCE)
            .amount(UPDATED_AMOUNT)
            .direction(UPDATED_DIRECTION)
            .relatedPaymentId(UPDATED_RELATED_PAYMENT_ID)
            .description(UPDATED_DESCRIPTION);

        restBankTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBankTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBankTransaction))
            )
            .andExpect(status().isOk());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBankTransactionToMatchAllProperties(updatedBankTransaction);
    }

    @Test
    @Transactional
    void putNonExistingBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBankTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankTransaction using partial update
        BankTransaction partialUpdatedBankTransaction = new BankTransaction();
        partialUpdatedBankTransaction.setId(bankTransaction.getId());

        partialUpdatedBankTransaction
            .bankAccountId(UPDATED_BANK_ACCOUNT_ID)
            .reference(UPDATED_REFERENCE)
            .amount(UPDATED_AMOUNT)
            .relatedPaymentId(UPDATED_RELATED_PAYMENT_ID);

        restBankTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBankTransaction))
            )
            .andExpect(status().isOk());

        // Validate the BankTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankTransactionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBankTransaction, bankTransaction),
            getPersistedBankTransaction(bankTransaction)
        );
    }

    @Test
    @Transactional
    void fullUpdateBankTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankTransaction using partial update
        BankTransaction partialUpdatedBankTransaction = new BankTransaction();
        partialUpdatedBankTransaction.setId(bankTransaction.getId());

        partialUpdatedBankTransaction
            .bankAccountId(UPDATED_BANK_ACCOUNT_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .reference(UPDATED_REFERENCE)
            .amount(UPDATED_AMOUNT)
            .direction(UPDATED_DIRECTION)
            .relatedPaymentId(UPDATED_RELATED_PAYMENT_ID)
            .description(UPDATED_DESCRIPTION);

        restBankTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBankTransaction))
            )
            .andExpect(status().isOk());

        // Validate the BankTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankTransactionUpdatableFieldsEquals(
            partialUpdatedBankTransaction,
            getPersistedBankTransaction(partialUpdatedBankTransaction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bankTransaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBankTransaction() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bankTransaction
        restBankTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, bankTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bankTransactionRepository.count();
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

    protected BankTransaction getPersistedBankTransaction(BankTransaction bankTransaction) {
        return bankTransactionRepository.findById(bankTransaction.getId()).orElseThrow();
    }

    protected void assertPersistedBankTransactionToMatchAllProperties(BankTransaction expectedBankTransaction) {
        assertBankTransactionAllPropertiesEquals(expectedBankTransaction, getPersistedBankTransaction(expectedBankTransaction));
    }

    protected void assertPersistedBankTransactionToMatchUpdatableProperties(BankTransaction expectedBankTransaction) {
        assertBankTransactionAllUpdatablePropertiesEquals(expectedBankTransaction, getPersistedBankTransaction(expectedBankTransaction));
    }
}
