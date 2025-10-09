package com.blitz.account.web.rest;

import static com.blitz.account.domain.BankAccountAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.BankAccount;
import com.blitz.account.repository.BankAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link BankAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BankAccountResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bank-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankAccountMockMvc;

    private BankAccount bankAccount;

    private BankAccount insertedBankAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createEntity() {
        return new BankAccount().name(DEFAULT_NAME).accountNumber(DEFAULT_ACCOUNT_NUMBER).bankName(DEFAULT_BANK_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createUpdatedEntity() {
        return new BankAccount().name(UPDATED_NAME).accountNumber(UPDATED_ACCOUNT_NUMBER).bankName(UPDATED_BANK_NAME);
    }

    @BeforeEach
    void initTest() {
        bankAccount = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBankAccount != null) {
            bankAccountRepository.delete(insertedBankAccount);
            insertedBankAccount = null;
        }
    }

    @Test
    @Transactional
    void createBankAccount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BankAccount
        var returnedBankAccount = om.readValue(
            restBankAccountMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankAccount)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BankAccount.class
        );

        // Validate the BankAccount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBankAccountUpdatableFieldsEquals(returnedBankAccount, getPersistedBankAccount(returnedBankAccount));

        insertedBankAccount = returnedBankAccount;
    }

    @Test
    @Transactional
    void createBankAccountWithExistingId() throws Exception {
        // Create the BankAccount with an existing ID
        bankAccount.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankAccount)))
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankAccount.setName(null);

        // Create the BankAccount, which fails.

        restBankAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankAccount)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankAccount.setAccountNumber(null);

        // Create the BankAccount, which fails.

        restBankAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankAccount)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBankAccounts() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)));
    }

    @Test
    @Transactional
    void getBankAccount() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get the bankAccount
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, bankAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankAccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME));
    }

    @Test
    @Transactional
    void getBankAccountsByIdFiltering() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        Long id = bankAccount.getId();

        defaultBankAccountFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBankAccountFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBankAccountFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name equals to
        defaultBankAccountFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name in
        defaultBankAccountFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name is not null
        defaultBankAccountFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name contains
        defaultBankAccountFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name does not contain
        defaultBankAccountFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber equals to
        defaultBankAccountFiltering("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER, "accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber in
        defaultBankAccountFiltering(
            "accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER,
            "accountNumber.in=" + UPDATED_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber is not null
        defaultBankAccountFiltering("accountNumber.specified=true", "accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber contains
        defaultBankAccountFiltering("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER, "accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBankAccountsByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where accountNumber does not contain
        defaultBankAccountFiltering(
            "accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER,
            "accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName equals to
        defaultBankAccountFiltering("bankName.equals=" + DEFAULT_BANK_NAME, "bankName.equals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName in
        defaultBankAccountFiltering("bankName.in=" + DEFAULT_BANK_NAME + "," + UPDATED_BANK_NAME, "bankName.in=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName is not null
        defaultBankAccountFiltering("bankName.specified=true", "bankName.specified=false");
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameContainsSomething() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName contains
        defaultBankAccountFiltering("bankName.contains=" + DEFAULT_BANK_NAME, "bankName.contains=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankAccountsByBankNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where bankName does not contain
        defaultBankAccountFiltering("bankName.doesNotContain=" + UPDATED_BANK_NAME, "bankName.doesNotContain=" + DEFAULT_BANK_NAME);
    }

    private void defaultBankAccountFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBankAccountShouldBeFound(shouldBeFound);
        defaultBankAccountShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankAccountShouldBeFound(String filter) throws Exception {
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)));

        // Check, that the count call also returns 1
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBankAccountShouldNotBeFound(String filter) throws Exception {
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBankAccount() throws Exception {
        // Get the bankAccount
        restBankAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBankAccount() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankAccount
        BankAccount updatedBankAccount = bankAccountRepository.findById(bankAccount.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBankAccount are not directly saved in db
        em.detach(updatedBankAccount);
        updatedBankAccount.name(UPDATED_NAME).accountNumber(UPDATED_ACCOUNT_NUMBER).bankName(UPDATED_BANK_NAME);

        restBankAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBankAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBankAccount))
            )
            .andExpect(status().isOk());

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBankAccountToMatchAllProperties(updatedBankAccount);
    }

    @Test
    @Transactional
    void putNonExistingBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankAccount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBankAccountWithPatch() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankAccount using partial update
        BankAccount partialUpdatedBankAccount = new BankAccount();
        partialUpdatedBankAccount.setId(bankAccount.getId());

        partialUpdatedBankAccount.name(UPDATED_NAME).accountNumber(UPDATED_ACCOUNT_NUMBER);

        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBankAccount))
            )
            .andExpect(status().isOk());

        // Validate the BankAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankAccountUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBankAccount, bankAccount),
            getPersistedBankAccount(bankAccount)
        );
    }

    @Test
    @Transactional
    void fullUpdateBankAccountWithPatch() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankAccount using partial update
        BankAccount partialUpdatedBankAccount = new BankAccount();
        partialUpdatedBankAccount.setId(bankAccount.getId());

        partialUpdatedBankAccount.name(UPDATED_NAME).accountNumber(UPDATED_ACCOUNT_NUMBER).bankName(UPDATED_BANK_NAME);

        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBankAccount))
            )
            .andExpect(status().isOk());

        // Validate the BankAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankAccountUpdatableFieldsEquals(partialUpdatedBankAccount, getPersistedBankAccount(partialUpdatedBankAccount));
    }

    @Test
    @Transactional
    void patchNonExistingBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankAccount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBankAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankAccountMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bankAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBankAccount() throws Exception {
        // Initialize the database
        insertedBankAccount = bankAccountRepository.saveAndFlush(bankAccount);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bankAccount
        restBankAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, bankAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bankAccountRepository.count();
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

    protected BankAccount getPersistedBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.findById(bankAccount.getId()).orElseThrow();
    }

    protected void assertPersistedBankAccountToMatchAllProperties(BankAccount expectedBankAccount) {
        assertBankAccountAllPropertiesEquals(expectedBankAccount, getPersistedBankAccount(expectedBankAccount));
    }

    protected void assertPersistedBankAccountToMatchUpdatableProperties(BankAccount expectedBankAccount) {
        assertBankAccountAllUpdatablePropertiesEquals(expectedBankAccount, getPersistedBankAccount(expectedBankAccount));
    }
}
