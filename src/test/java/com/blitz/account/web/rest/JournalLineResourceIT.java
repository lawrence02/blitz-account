package com.blitz.account.web.rest;

import static com.blitz.account.domain.JournalLineAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.JournalLine;
import com.blitz.account.repository.JournalLineRepository;
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
 * Integration tests for the {@link JournalLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JournalLineResourceIT {

    private static final Long DEFAULT_JOURNAL_ID = 1L;
    private static final Long UPDATED_JOURNAL_ID = 2L;

    private static final Long DEFAULT_ACCOUNT_ID = 1L;
    private static final Long UPDATED_ACCOUNT_ID = 2L;

    private static final BigDecimal DEFAULT_DEBIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEBIT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CREDIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/journal-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JournalLineRepository journalLineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJournalLineMockMvc;

    private JournalLine journalLine;

    private JournalLine insertedJournalLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JournalLine createEntity() {
        return new JournalLine().journalId(DEFAULT_JOURNAL_ID).accountId(DEFAULT_ACCOUNT_ID).debit(DEFAULT_DEBIT).credit(DEFAULT_CREDIT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JournalLine createUpdatedEntity() {
        return new JournalLine().journalId(UPDATED_JOURNAL_ID).accountId(UPDATED_ACCOUNT_ID).debit(UPDATED_DEBIT).credit(UPDATED_CREDIT);
    }

    @BeforeEach
    void initTest() {
        journalLine = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedJournalLine != null) {
            journalLineRepository.delete(insertedJournalLine);
            insertedJournalLine = null;
        }
    }

    @Test
    @Transactional
    void createJournalLine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the JournalLine
        var returnedJournalLine = om.readValue(
            restJournalLineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalLine)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            JournalLine.class
        );

        // Validate the JournalLine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertJournalLineUpdatableFieldsEquals(returnedJournalLine, getPersistedJournalLine(returnedJournalLine));

        insertedJournalLine = returnedJournalLine;
    }

    @Test
    @Transactional
    void createJournalLineWithExistingId() throws Exception {
        // Create the JournalLine with an existing ID
        journalLine.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJournalLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalLine)))
            .andExpect(status().isBadRequest());

        // Validate the JournalLine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkJournalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        journalLine.setJournalId(null);

        // Create the JournalLine, which fails.

        restJournalLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalLine)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        journalLine.setAccountId(null);

        // Create the JournalLine, which fails.

        restJournalLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalLine)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJournalLines() throws Exception {
        // Initialize the database
        insertedJournalLine = journalLineRepository.saveAndFlush(journalLine);

        // Get all the journalLineList
        restJournalLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journalLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].journalId").value(hasItem(DEFAULT_JOURNAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].debit").value(hasItem(sameNumber(DEFAULT_DEBIT))))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(sameNumber(DEFAULT_CREDIT))));
    }

    @Test
    @Transactional
    void getJournalLine() throws Exception {
        // Initialize the database
        insertedJournalLine = journalLineRepository.saveAndFlush(journalLine);

        // Get the journalLine
        restJournalLineMockMvc
            .perform(get(ENTITY_API_URL_ID, journalLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(journalLine.getId().intValue()))
            .andExpect(jsonPath("$.journalId").value(DEFAULT_JOURNAL_ID.intValue()))
            .andExpect(jsonPath("$.accountId").value(DEFAULT_ACCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.debit").value(sameNumber(DEFAULT_DEBIT)))
            .andExpect(jsonPath("$.credit").value(sameNumber(DEFAULT_CREDIT)));
    }

    @Test
    @Transactional
    void getNonExistingJournalLine() throws Exception {
        // Get the journalLine
        restJournalLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJournalLine() throws Exception {
        // Initialize the database
        insertedJournalLine = journalLineRepository.saveAndFlush(journalLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the journalLine
        JournalLine updatedJournalLine = journalLineRepository.findById(journalLine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJournalLine are not directly saved in db
        em.detach(updatedJournalLine);
        updatedJournalLine.journalId(UPDATED_JOURNAL_ID).accountId(UPDATED_ACCOUNT_ID).debit(UPDATED_DEBIT).credit(UPDATED_CREDIT);

        restJournalLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJournalLine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedJournalLine))
            )
            .andExpect(status().isOk());

        // Validate the JournalLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJournalLineToMatchAllProperties(updatedJournalLine);
    }

    @Test
    @Transactional
    void putNonExistingJournalLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJournalLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, journalLine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(journalLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the JournalLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJournalLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(journalLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the JournalLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJournalLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JournalLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJournalLineWithPatch() throws Exception {
        // Initialize the database
        insertedJournalLine = journalLineRepository.saveAndFlush(journalLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the journalLine using partial update
        JournalLine partialUpdatedJournalLine = new JournalLine();
        partialUpdatedJournalLine.setId(journalLine.getId());

        partialUpdatedJournalLine.journalId(UPDATED_JOURNAL_ID).accountId(UPDATED_ACCOUNT_ID).debit(UPDATED_DEBIT);

        restJournalLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJournalLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJournalLine))
            )
            .andExpect(status().isOk());

        // Validate the JournalLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJournalLineUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedJournalLine, journalLine),
            getPersistedJournalLine(journalLine)
        );
    }

    @Test
    @Transactional
    void fullUpdateJournalLineWithPatch() throws Exception {
        // Initialize the database
        insertedJournalLine = journalLineRepository.saveAndFlush(journalLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the journalLine using partial update
        JournalLine partialUpdatedJournalLine = new JournalLine();
        partialUpdatedJournalLine.setId(journalLine.getId());

        partialUpdatedJournalLine.journalId(UPDATED_JOURNAL_ID).accountId(UPDATED_ACCOUNT_ID).debit(UPDATED_DEBIT).credit(UPDATED_CREDIT);

        restJournalLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJournalLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJournalLine))
            )
            .andExpect(status().isOk());

        // Validate the JournalLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJournalLineUpdatableFieldsEquals(partialUpdatedJournalLine, getPersistedJournalLine(partialUpdatedJournalLine));
    }

    @Test
    @Transactional
    void patchNonExistingJournalLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJournalLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, journalLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(journalLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the JournalLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJournalLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(journalLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the JournalLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJournalLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalLineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(journalLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JournalLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJournalLine() throws Exception {
        // Initialize the database
        insertedJournalLine = journalLineRepository.saveAndFlush(journalLine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the journalLine
        restJournalLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, journalLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return journalLineRepository.count();
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

    protected JournalLine getPersistedJournalLine(JournalLine journalLine) {
        return journalLineRepository.findById(journalLine.getId()).orElseThrow();
    }

    protected void assertPersistedJournalLineToMatchAllProperties(JournalLine expectedJournalLine) {
        assertJournalLineAllPropertiesEquals(expectedJournalLine, getPersistedJournalLine(expectedJournalLine));
    }

    protected void assertPersistedJournalLineToMatchUpdatableProperties(JournalLine expectedJournalLine) {
        assertJournalLineAllUpdatablePropertiesEquals(expectedJournalLine, getPersistedJournalLine(expectedJournalLine));
    }
}
