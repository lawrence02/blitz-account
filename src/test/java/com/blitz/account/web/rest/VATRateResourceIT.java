package com.blitz.account.web.rest;

import static com.blitz.account.domain.VATRateAsserts.*;
import static com.blitz.account.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blitz.account.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blitz.account.IntegrationTest;
import com.blitz.account.domain.VATRate;
import com.blitz.account.repository.VATRateRepository;
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
 * Integration tests for the {@link VATRateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VATRateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENTAGE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/vat-rates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VATRateRepository vATRateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVATRateMockMvc;

    private VATRate vATRate;

    private VATRate insertedVATRate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VATRate createEntity() {
        return new VATRate().name(DEFAULT_NAME).percentage(DEFAULT_PERCENTAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VATRate createUpdatedEntity() {
        return new VATRate().name(UPDATED_NAME).percentage(UPDATED_PERCENTAGE);
    }

    @BeforeEach
    void initTest() {
        vATRate = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVATRate != null) {
            vATRateRepository.delete(insertedVATRate);
            insertedVATRate = null;
        }
    }

    @Test
    @Transactional
    void createVATRate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VATRate
        var returnedVATRate = om.readValue(
            restVATRateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vATRate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VATRate.class
        );

        // Validate the VATRate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVATRateUpdatableFieldsEquals(returnedVATRate, getPersistedVATRate(returnedVATRate));

        insertedVATRate = returnedVATRate;
    }

    @Test
    @Transactional
    void createVATRateWithExistingId() throws Exception {
        // Create the VATRate with an existing ID
        vATRate.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVATRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vATRate)))
            .andExpect(status().isBadRequest());

        // Validate the VATRate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vATRate.setName(null);

        // Create the VATRate, which fails.

        restVATRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vATRate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPercentageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vATRate.setPercentage(null);

        // Create the VATRate, which fails.

        restVATRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vATRate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVATRates() throws Exception {
        // Initialize the database
        insertedVATRate = vATRateRepository.saveAndFlush(vATRate);

        // Get all the vATRateList
        restVATRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vATRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(sameNumber(DEFAULT_PERCENTAGE))));
    }

    @Test
    @Transactional
    void getVATRate() throws Exception {
        // Initialize the database
        insertedVATRate = vATRateRepository.saveAndFlush(vATRate);

        // Get the vATRate
        restVATRateMockMvc
            .perform(get(ENTITY_API_URL_ID, vATRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vATRate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.percentage").value(sameNumber(DEFAULT_PERCENTAGE)));
    }

    @Test
    @Transactional
    void getNonExistingVATRate() throws Exception {
        // Get the vATRate
        restVATRateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVATRate() throws Exception {
        // Initialize the database
        insertedVATRate = vATRateRepository.saveAndFlush(vATRate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vATRate
        VATRate updatedVATRate = vATRateRepository.findById(vATRate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVATRate are not directly saved in db
        em.detach(updatedVATRate);
        updatedVATRate.name(UPDATED_NAME).percentage(UPDATED_PERCENTAGE);

        restVATRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVATRate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVATRate))
            )
            .andExpect(status().isOk());

        // Validate the VATRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVATRateToMatchAllProperties(updatedVATRate);
    }

    @Test
    @Transactional
    void putNonExistingVATRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vATRate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVATRateMockMvc
            .perform(put(ENTITY_API_URL_ID, vATRate.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vATRate)))
            .andExpect(status().isBadRequest());

        // Validate the VATRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVATRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vATRate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVATRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vATRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the VATRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVATRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vATRate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVATRateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vATRate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VATRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVATRateWithPatch() throws Exception {
        // Initialize the database
        insertedVATRate = vATRateRepository.saveAndFlush(vATRate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vATRate using partial update
        VATRate partialUpdatedVATRate = new VATRate();
        partialUpdatedVATRate.setId(vATRate.getId());

        partialUpdatedVATRate.name(UPDATED_NAME);

        restVATRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVATRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVATRate))
            )
            .andExpect(status().isOk());

        // Validate the VATRate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVATRateUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVATRate, vATRate), getPersistedVATRate(vATRate));
    }

    @Test
    @Transactional
    void fullUpdateVATRateWithPatch() throws Exception {
        // Initialize the database
        insertedVATRate = vATRateRepository.saveAndFlush(vATRate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vATRate using partial update
        VATRate partialUpdatedVATRate = new VATRate();
        partialUpdatedVATRate.setId(vATRate.getId());

        partialUpdatedVATRate.name(UPDATED_NAME).percentage(UPDATED_PERCENTAGE);

        restVATRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVATRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVATRate))
            )
            .andExpect(status().isOk());

        // Validate the VATRate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVATRateUpdatableFieldsEquals(partialUpdatedVATRate, getPersistedVATRate(partialUpdatedVATRate));
    }

    @Test
    @Transactional
    void patchNonExistingVATRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vATRate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVATRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vATRate.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vATRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the VATRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVATRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vATRate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVATRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vATRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the VATRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVATRate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vATRate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVATRateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vATRate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VATRate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVATRate() throws Exception {
        // Initialize the database
        insertedVATRate = vATRateRepository.saveAndFlush(vATRate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vATRate
        restVATRateMockMvc
            .perform(delete(ENTITY_API_URL_ID, vATRate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vATRateRepository.count();
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

    protected VATRate getPersistedVATRate(VATRate vATRate) {
        return vATRateRepository.findById(vATRate.getId()).orElseThrow();
    }

    protected void assertPersistedVATRateToMatchAllProperties(VATRate expectedVATRate) {
        assertVATRateAllPropertiesEquals(expectedVATRate, getPersistedVATRate(expectedVATRate));
    }

    protected void assertPersistedVATRateToMatchUpdatableProperties(VATRate expectedVATRate) {
        assertVATRateAllUpdatablePropertiesEquals(expectedVATRate, getPersistedVATRate(expectedVATRate));
    }
}
