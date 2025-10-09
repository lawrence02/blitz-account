package com.blitz.account.web.rest;

import static com.blitz.account.domain.ChartOfAccountAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.ChartOfAccount;
import com.blitz.account.domain.enumeration.AccountType;
import com.blitz.account.repository.ChartOfAccountRepository;
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
 * Integration tests for the {@link ChartOfAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChartOfAccountResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.ASSET;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.LIABILITY;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_INITIAL_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_INITIAL_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_INITIAL_BALANCE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CURRENT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_CURRENT_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/chart-of-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChartOfAccountMockMvc;

    private ChartOfAccount chartOfAccount;

    private ChartOfAccount insertedChartOfAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChartOfAccount createEntity() {
        return new ChartOfAccount()
            .name(DEFAULT_NAME)
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .code(DEFAULT_CODE)
            .initialBalance(DEFAULT_INITIAL_BALANCE)
            .currentBalance(DEFAULT_CURRENT_BALANCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChartOfAccount createUpdatedEntity() {
        return new ChartOfAccount()
            .name(UPDATED_NAME)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .code(UPDATED_CODE)
            .initialBalance(UPDATED_INITIAL_BALANCE)
            .currentBalance(UPDATED_CURRENT_BALANCE);
    }

    @BeforeEach
    void initTest() {
        chartOfAccount = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedChartOfAccount != null) {
            chartOfAccountRepository.delete(insertedChartOfAccount);
            insertedChartOfAccount = null;
        }
    }

    @Test
    @Transactional
    void createChartOfAccount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChartOfAccount
        var returnedChartOfAccount = om.readValue(
            restChartOfAccountMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chartOfAccount)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChartOfAccount.class
        );

        // Validate the ChartOfAccount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertChartOfAccountUpdatableFieldsEquals(returnedChartOfAccount, getPersistedChartOfAccount(returnedChartOfAccount));

        insertedChartOfAccount = returnedChartOfAccount;
    }

    @Test
    @Transactional
    void createChartOfAccountWithExistingId() throws Exception {
        // Create the ChartOfAccount with an existing ID
        chartOfAccount.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChartOfAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chartOfAccount)))
            .andExpect(status().isBadRequest());

        // Validate the ChartOfAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chartOfAccount.setName(null);

        // Create the ChartOfAccount, which fails.

        restChartOfAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chartOfAccount)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chartOfAccount.setAccountType(null);

        // Create the ChartOfAccount, which fails.

        restChartOfAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chartOfAccount)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chartOfAccount.setCode(null);

        // Create the ChartOfAccount, which fails.

        restChartOfAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chartOfAccount)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChartOfAccounts() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList
        restChartOfAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chartOfAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].initialBalance").value(hasItem(sameNumber(DEFAULT_INITIAL_BALANCE))))
            .andExpect(jsonPath("$.[*].currentBalance").value(hasItem(sameNumber(DEFAULT_CURRENT_BALANCE))));
    }

    @Test
    @Transactional
    void getChartOfAccount() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get the chartOfAccount
        restChartOfAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, chartOfAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chartOfAccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.initialBalance").value(sameNumber(DEFAULT_INITIAL_BALANCE)))
            .andExpect(jsonPath("$.currentBalance").value(sameNumber(DEFAULT_CURRENT_BALANCE)));
    }

    @Test
    @Transactional
    void getChartOfAccountsByIdFiltering() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        Long id = chartOfAccount.getId();

        defaultChartOfAccountFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultChartOfAccountFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultChartOfAccountFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where name equals to
        defaultChartOfAccountFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where name in
        defaultChartOfAccountFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where name is not null
        defaultChartOfAccountFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where name contains
        defaultChartOfAccountFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where name does not contain
        defaultChartOfAccountFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where accountType equals to
        defaultChartOfAccountFiltering("accountType.equals=" + DEFAULT_ACCOUNT_TYPE, "accountType.equals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByAccountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where accountType in
        defaultChartOfAccountFiltering(
            "accountType.in=" + DEFAULT_ACCOUNT_TYPE + "," + UPDATED_ACCOUNT_TYPE,
            "accountType.in=" + UPDATED_ACCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByAccountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where accountType is not null
        defaultChartOfAccountFiltering("accountType.specified=true", "accountType.specified=false");
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where code equals to
        defaultChartOfAccountFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where code in
        defaultChartOfAccountFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where code is not null
        defaultChartOfAccountFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where code contains
        defaultChartOfAccountFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where code does not contain
        defaultChartOfAccountFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByInitialBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where initialBalance equals to
        defaultChartOfAccountFiltering(
            "initialBalance.equals=" + DEFAULT_INITIAL_BALANCE,
            "initialBalance.equals=" + UPDATED_INITIAL_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByInitialBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where initialBalance in
        defaultChartOfAccountFiltering(
            "initialBalance.in=" + DEFAULT_INITIAL_BALANCE + "," + UPDATED_INITIAL_BALANCE,
            "initialBalance.in=" + UPDATED_INITIAL_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByInitialBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where initialBalance is not null
        defaultChartOfAccountFiltering("initialBalance.specified=true", "initialBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByInitialBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where initialBalance is greater than or equal to
        defaultChartOfAccountFiltering(
            "initialBalance.greaterThanOrEqual=" + DEFAULT_INITIAL_BALANCE,
            "initialBalance.greaterThanOrEqual=" + UPDATED_INITIAL_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByInitialBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where initialBalance is less than or equal to
        defaultChartOfAccountFiltering(
            "initialBalance.lessThanOrEqual=" + DEFAULT_INITIAL_BALANCE,
            "initialBalance.lessThanOrEqual=" + SMALLER_INITIAL_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByInitialBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where initialBalance is less than
        defaultChartOfAccountFiltering(
            "initialBalance.lessThan=" + UPDATED_INITIAL_BALANCE,
            "initialBalance.lessThan=" + DEFAULT_INITIAL_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByInitialBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where initialBalance is greater than
        defaultChartOfAccountFiltering(
            "initialBalance.greaterThan=" + SMALLER_INITIAL_BALANCE,
            "initialBalance.greaterThan=" + DEFAULT_INITIAL_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCurrentBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where currentBalance equals to
        defaultChartOfAccountFiltering(
            "currentBalance.equals=" + DEFAULT_CURRENT_BALANCE,
            "currentBalance.equals=" + UPDATED_CURRENT_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCurrentBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where currentBalance in
        defaultChartOfAccountFiltering(
            "currentBalance.in=" + DEFAULT_CURRENT_BALANCE + "," + UPDATED_CURRENT_BALANCE,
            "currentBalance.in=" + UPDATED_CURRENT_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCurrentBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where currentBalance is not null
        defaultChartOfAccountFiltering("currentBalance.specified=true", "currentBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCurrentBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where currentBalance is greater than or equal to
        defaultChartOfAccountFiltering(
            "currentBalance.greaterThanOrEqual=" + DEFAULT_CURRENT_BALANCE,
            "currentBalance.greaterThanOrEqual=" + UPDATED_CURRENT_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCurrentBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where currentBalance is less than or equal to
        defaultChartOfAccountFiltering(
            "currentBalance.lessThanOrEqual=" + DEFAULT_CURRENT_BALANCE,
            "currentBalance.lessThanOrEqual=" + SMALLER_CURRENT_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCurrentBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where currentBalance is less than
        defaultChartOfAccountFiltering(
            "currentBalance.lessThan=" + UPDATED_CURRENT_BALANCE,
            "currentBalance.lessThan=" + DEFAULT_CURRENT_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllChartOfAccountsByCurrentBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        // Get all the chartOfAccountList where currentBalance is greater than
        defaultChartOfAccountFiltering(
            "currentBalance.greaterThan=" + SMALLER_CURRENT_BALANCE,
            "currentBalance.greaterThan=" + DEFAULT_CURRENT_BALANCE
        );
    }

    private void defaultChartOfAccountFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultChartOfAccountShouldBeFound(shouldBeFound);
        defaultChartOfAccountShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChartOfAccountShouldBeFound(String filter) throws Exception {
        restChartOfAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chartOfAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].initialBalance").value(hasItem(sameNumber(DEFAULT_INITIAL_BALANCE))))
            .andExpect(jsonPath("$.[*].currentBalance").value(hasItem(sameNumber(DEFAULT_CURRENT_BALANCE))));

        // Check, that the count call also returns 1
        restChartOfAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChartOfAccountShouldNotBeFound(String filter) throws Exception {
        restChartOfAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChartOfAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChartOfAccount() throws Exception {
        // Get the chartOfAccount
        restChartOfAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChartOfAccount() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chartOfAccount
        ChartOfAccount updatedChartOfAccount = chartOfAccountRepository.findById(chartOfAccount.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChartOfAccount are not directly saved in db
        em.detach(updatedChartOfAccount);
        updatedChartOfAccount
            .name(UPDATED_NAME)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .code(UPDATED_CODE)
            .initialBalance(UPDATED_INITIAL_BALANCE)
            .currentBalance(UPDATED_CURRENT_BALANCE);

        restChartOfAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChartOfAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedChartOfAccount))
            )
            .andExpect(status().isOk());

        // Validate the ChartOfAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChartOfAccountToMatchAllProperties(updatedChartOfAccount);
    }

    @Test
    @Transactional
    void putNonExistingChartOfAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chartOfAccount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChartOfAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chartOfAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chartOfAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChartOfAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChartOfAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chartOfAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChartOfAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chartOfAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChartOfAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChartOfAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chartOfAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChartOfAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chartOfAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChartOfAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChartOfAccountWithPatch() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chartOfAccount using partial update
        ChartOfAccount partialUpdatedChartOfAccount = new ChartOfAccount();
        partialUpdatedChartOfAccount.setId(chartOfAccount.getId());

        partialUpdatedChartOfAccount.initialBalance(UPDATED_INITIAL_BALANCE);

        restChartOfAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChartOfAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChartOfAccount))
            )
            .andExpect(status().isOk());

        // Validate the ChartOfAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChartOfAccountUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedChartOfAccount, chartOfAccount),
            getPersistedChartOfAccount(chartOfAccount)
        );
    }

    @Test
    @Transactional
    void fullUpdateChartOfAccountWithPatch() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chartOfAccount using partial update
        ChartOfAccount partialUpdatedChartOfAccount = new ChartOfAccount();
        partialUpdatedChartOfAccount.setId(chartOfAccount.getId());

        partialUpdatedChartOfAccount
            .name(UPDATED_NAME)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .code(UPDATED_CODE)
            .initialBalance(UPDATED_INITIAL_BALANCE)
            .currentBalance(UPDATED_CURRENT_BALANCE);

        restChartOfAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChartOfAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChartOfAccount))
            )
            .andExpect(status().isOk());

        // Validate the ChartOfAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChartOfAccountUpdatableFieldsEquals(partialUpdatedChartOfAccount, getPersistedChartOfAccount(partialUpdatedChartOfAccount));
    }

    @Test
    @Transactional
    void patchNonExistingChartOfAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chartOfAccount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChartOfAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chartOfAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chartOfAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChartOfAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChartOfAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chartOfAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChartOfAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chartOfAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChartOfAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChartOfAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chartOfAccount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChartOfAccountMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chartOfAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChartOfAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChartOfAccount() throws Exception {
        // Initialize the database
        insertedChartOfAccount = chartOfAccountRepository.saveAndFlush(chartOfAccount);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chartOfAccount
        restChartOfAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, chartOfAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chartOfAccountRepository.count();
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

    protected ChartOfAccount getPersistedChartOfAccount(ChartOfAccount chartOfAccount) {
        return chartOfAccountRepository.findById(chartOfAccount.getId()).orElseThrow();
    }

    protected void assertPersistedChartOfAccountToMatchAllProperties(ChartOfAccount expectedChartOfAccount) {
        assertChartOfAccountAllPropertiesEquals(expectedChartOfAccount, getPersistedChartOfAccount(expectedChartOfAccount));
    }

    protected void assertPersistedChartOfAccountToMatchUpdatableProperties(ChartOfAccount expectedChartOfAccount) {
        assertChartOfAccountAllUpdatablePropertiesEquals(expectedChartOfAccount, getPersistedChartOfAccount(expectedChartOfAccount));
    }
}
