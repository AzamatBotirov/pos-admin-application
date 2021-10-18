package com.mycompany.pos.web.rest;

import static com.mycompany.pos.web.rest.TestUtil.sameInstant;
import static com.mycompany.pos.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.pos.IntegrationTest;
import com.mycompany.pos.domain.MarketBase;
import com.mycompany.pos.repository.MarketBaseRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MarketBaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarketBaseResourceIT {

    private static final String DEFAULT_QUANTITY = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CURRENT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_PRICE = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/market-bases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MarketBaseRepository marketBaseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarketBaseMockMvc;

    private MarketBase marketBase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketBase createEntity(EntityManager em) {
        MarketBase marketBase = new MarketBase()
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .currentPrice(DEFAULT_CURRENT_PRICE)
            .createDate(DEFAULT_CREATE_DATE)
            .date(DEFAULT_DATE);
        return marketBase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketBase createUpdatedEntity(EntityManager em) {
        MarketBase marketBase = new MarketBase()
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .currentPrice(UPDATED_CURRENT_PRICE)
            .createDate(UPDATED_CREATE_DATE)
            .date(UPDATED_DATE);
        return marketBase;
    }

    @BeforeEach
    public void initTest() {
        marketBase = createEntity(em);
    }

    @Test
    @Transactional
    void createMarketBase() throws Exception {
        int databaseSizeBeforeCreate = marketBaseRepository.findAll().size();
        // Create the MarketBase
        restMarketBaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketBase)))
            .andExpect(status().isCreated());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeCreate + 1);
        MarketBase testMarketBase = marketBaseList.get(marketBaseList.size() - 1);
        assertThat(testMarketBase.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testMarketBase.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testMarketBase.getCurrentPrice()).isEqualByComparingTo(DEFAULT_CURRENT_PRICE);
        assertThat(testMarketBase.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testMarketBase.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createMarketBaseWithExistingId() throws Exception {
        // Create the MarketBase with an existing ID
        marketBase.setId(1L);

        int databaseSizeBeforeCreate = marketBaseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarketBaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketBase)))
            .andExpect(status().isBadRequest());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMarketBases() throws Exception {
        // Initialize the database
        marketBaseRepository.saveAndFlush(marketBase);

        // Get all the marketBaseList
        restMarketBaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketBase.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].currentPrice").value(hasItem(sameNumber(DEFAULT_CURRENT_PRICE))))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    void getMarketBase() throws Exception {
        // Initialize the database
        marketBaseRepository.saveAndFlush(marketBase);

        // Get the marketBase
        restMarketBaseMockMvc
            .perform(get(ENTITY_API_URL_ID, marketBase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marketBase.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.currentPrice").value(sameNumber(DEFAULT_CURRENT_PRICE)))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingMarketBase() throws Exception {
        // Get the marketBase
        restMarketBaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMarketBase() throws Exception {
        // Initialize the database
        marketBaseRepository.saveAndFlush(marketBase);

        int databaseSizeBeforeUpdate = marketBaseRepository.findAll().size();

        // Update the marketBase
        MarketBase updatedMarketBase = marketBaseRepository.findById(marketBase.getId()).get();
        // Disconnect from session so that the updates on updatedMarketBase are not directly saved in db
        em.detach(updatedMarketBase);
        updatedMarketBase
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .currentPrice(UPDATED_CURRENT_PRICE)
            .createDate(UPDATED_CREATE_DATE)
            .date(UPDATED_DATE);

        restMarketBaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMarketBase.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMarketBase))
            )
            .andExpect(status().isOk());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeUpdate);
        MarketBase testMarketBase = marketBaseList.get(marketBaseList.size() - 1);
        assertThat(testMarketBase.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testMarketBase.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMarketBase.getCurrentPrice()).isEqualTo(UPDATED_CURRENT_PRICE);
        assertThat(testMarketBase.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testMarketBase.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMarketBase() throws Exception {
        int databaseSizeBeforeUpdate = marketBaseRepository.findAll().size();
        marketBase.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketBaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marketBase.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marketBase))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarketBase() throws Exception {
        int databaseSizeBeforeUpdate = marketBaseRepository.findAll().size();
        marketBase.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketBaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marketBase))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarketBase() throws Exception {
        int databaseSizeBeforeUpdate = marketBaseRepository.findAll().size();
        marketBase.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketBaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marketBase)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarketBaseWithPatch() throws Exception {
        // Initialize the database
        marketBaseRepository.saveAndFlush(marketBase);

        int databaseSizeBeforeUpdate = marketBaseRepository.findAll().size();

        // Update the marketBase using partial update
        MarketBase partialUpdatedMarketBase = new MarketBase();
        partialUpdatedMarketBase.setId(marketBase.getId());

        partialUpdatedMarketBase.currentPrice(UPDATED_CURRENT_PRICE);

        restMarketBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketBase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarketBase))
            )
            .andExpect(status().isOk());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeUpdate);
        MarketBase testMarketBase = marketBaseList.get(marketBaseList.size() - 1);
        assertThat(testMarketBase.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testMarketBase.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testMarketBase.getCurrentPrice()).isEqualByComparingTo(UPDATED_CURRENT_PRICE);
        assertThat(testMarketBase.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testMarketBase.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMarketBaseWithPatch() throws Exception {
        // Initialize the database
        marketBaseRepository.saveAndFlush(marketBase);

        int databaseSizeBeforeUpdate = marketBaseRepository.findAll().size();

        // Update the marketBase using partial update
        MarketBase partialUpdatedMarketBase = new MarketBase();
        partialUpdatedMarketBase.setId(marketBase.getId());

        partialUpdatedMarketBase
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .currentPrice(UPDATED_CURRENT_PRICE)
            .createDate(UPDATED_CREATE_DATE)
            .date(UPDATED_DATE);

        restMarketBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketBase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarketBase))
            )
            .andExpect(status().isOk());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeUpdate);
        MarketBase testMarketBase = marketBaseList.get(marketBaseList.size() - 1);
        assertThat(testMarketBase.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testMarketBase.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testMarketBase.getCurrentPrice()).isEqualByComparingTo(UPDATED_CURRENT_PRICE);
        assertThat(testMarketBase.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testMarketBase.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMarketBase() throws Exception {
        int databaseSizeBeforeUpdate = marketBaseRepository.findAll().size();
        marketBase.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marketBase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marketBase))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarketBase() throws Exception {
        int databaseSizeBeforeUpdate = marketBaseRepository.findAll().size();
        marketBase.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marketBase))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarketBase() throws Exception {
        int databaseSizeBeforeUpdate = marketBaseRepository.findAll().size();
        marketBase.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketBaseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(marketBase))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarketBase in the database
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarketBase() throws Exception {
        // Initialize the database
        marketBaseRepository.saveAndFlush(marketBase);

        int databaseSizeBeforeDelete = marketBaseRepository.findAll().size();

        // Delete the marketBase
        restMarketBaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, marketBase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MarketBase> marketBaseList = marketBaseRepository.findAll();
        assertThat(marketBaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
