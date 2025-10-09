package com.blitz.account.web.rest;

import static com.blitz.account.domain.JournalAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.Journal;
import com.blitz.account.repository.JournalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link JournalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JournalResourceIT {

    private static final Instant DEFAULT_JOURNAL_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_JOURNAL_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/journals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJournalMockMvc;

    private Journal journal;

    private Journal insertedJournal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journal createEntity() {
        return new Journal().journalDate(DEFAULT_JOURNAL_DATE).reference(DEFAULT_REFERENCE).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journal createUpdatedEntity() {
        return new Journal().journalDate(UPDATED_JOURNAL_DATE).reference(UPDATED_REFERENCE).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        journal = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedJournal != null) {
            journalRepository.delete(insertedJournal);
            insertedJournal = null;
        }
    }

    @Test
    @Transactional
    void createJournal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Journal
        var returnedJournal = om.readValue(
            restJournalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journal)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Journal.class
        );

        // Validate the Journal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertJournalUpdatableFieldsEquals(returnedJournal, getPersistedJournal(returnedJournal));

        insertedJournal = returnedJournal;
    }

    @Test
    @Transactional
    void createJournalWithExistingId() throws Exception {
        // Create the Journal with an existing ID
        journal.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJournalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journal)))
            .andExpect(status().isBadRequest());

        // Validate the Journal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkJournalDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        journal.setJournalDate(null);

        // Create the Journal, which fails.

        restJournalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journal)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJournals() throws Exception {
        // Initialize the database
        insertedJournal = journalRepository.saveAndFlush(journal);

        // Get all the journalList
        restJournalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journal.getId().intValue())))
            .andExpect(jsonPath("$.[*].journalDate").value(hasItem(DEFAULT_JOURNAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getJournal() throws Exception {
        // Initialize the database
        insertedJournal = journalRepository.saveAndFlush(journal);

        // Get the journal
        restJournalMockMvc
            .perform(get(ENTITY_API_URL_ID, journal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(journal.getId().intValue()))
            .andExpect(jsonPath("$.journalDate").value(DEFAULT_JOURNAL_DATE.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingJournal() throws Exception {
        // Get the journal
        restJournalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJournal() throws Exception {
        // Initialize the database
        insertedJournal = journalRepository.saveAndFlush(journal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the journal
        Journal updatedJournal = journalRepository.findById(journal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJournal are not directly saved in db
        em.detach(updatedJournal);
        updatedJournal.journalDate(UPDATED_JOURNAL_DATE).reference(UPDATED_REFERENCE).description(UPDATED_DESCRIPTION);

        restJournalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJournal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedJournal))
            )
            .andExpect(status().isOk());

        // Validate the Journal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJournalToMatchAllProperties(updatedJournal);
    }

    @Test
    @Transactional
    void putNonExistingJournal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journal.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJournalMockMvc
            .perform(put(ENTITY_API_URL_ID, journal.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journal)))
            .andExpect(status().isBadRequest());

        // Validate the Journal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJournal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(journal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJournal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Journal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJournalWithPatch() throws Exception {
        // Initialize the database
        insertedJournal = journalRepository.saveAndFlush(journal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the journal using partial update
        Journal partialUpdatedJournal = new Journal();
        partialUpdatedJournal.setId(journal.getId());

        partialUpdatedJournal.description(UPDATED_DESCRIPTION);

        restJournalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJournal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJournal))
            )
            .andExpect(status().isOk());

        // Validate the Journal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJournalUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedJournal, journal), getPersistedJournal(journal));
    }

    @Test
    @Transactional
    void fullUpdateJournalWithPatch() throws Exception {
        // Initialize the database
        insertedJournal = journalRepository.saveAndFlush(journal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the journal using partial update
        Journal partialUpdatedJournal = new Journal();
        partialUpdatedJournal.setId(journal.getId());

        partialUpdatedJournal.journalDate(UPDATED_JOURNAL_DATE).reference(UPDATED_REFERENCE).description(UPDATED_DESCRIPTION);

        restJournalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJournal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJournal))
            )
            .andExpect(status().isOk());

        // Validate the Journal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJournalUpdatableFieldsEquals(partialUpdatedJournal, getPersistedJournal(partialUpdatedJournal));
    }

    @Test
    @Transactional
    void patchNonExistingJournal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journal.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJournalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, journal.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(journal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJournal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(journal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJournal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(journal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Journal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJournal() throws Exception {
        // Initialize the database
        insertedJournal = journalRepository.saveAndFlush(journal);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the journal
        restJournalMockMvc
            .perform(delete(ENTITY_API_URL_ID, journal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return journalRepository.count();
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

    protected Journal getPersistedJournal(Journal journal) {
        return journalRepository.findById(journal.getId()).orElseThrow();
    }

    protected void assertPersistedJournalToMatchAllProperties(Journal expectedJournal) {
        assertJournalAllPropertiesEquals(expectedJournal, getPersistedJournal(expectedJournal));
    }

    protected void assertPersistedJournalToMatchUpdatableProperties(Journal expectedJournal) {
        assertJournalAllUpdatablePropertiesEquals(expectedJournal, getPersistedJournal(expectedJournal));
    }
}
