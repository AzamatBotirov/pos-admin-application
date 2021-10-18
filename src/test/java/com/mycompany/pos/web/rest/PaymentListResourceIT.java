package com.mycompany.pos.web.rest;

import static com.mycompany.pos.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.pos.IntegrationTest;
import com.mycompany.pos.domain.PaymentList;
import com.mycompany.pos.repository.PaymentListRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PaymentListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentListResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_SUMMA = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUMMA = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/payment-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentListRepository paymentListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentListMockMvc;

    private PaymentList paymentList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentList createEntity(EntityManager em) {
        PaymentList paymentList = new PaymentList().quantity(DEFAULT_QUANTITY).summa(DEFAULT_SUMMA);
        return paymentList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentList createUpdatedEntity(EntityManager em) {
        PaymentList paymentList = new PaymentList().quantity(UPDATED_QUANTITY).summa(UPDATED_SUMMA);
        return paymentList;
    }

    @BeforeEach
    public void initTest() {
        paymentList = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentList() throws Exception {
        int databaseSizeBeforeCreate = paymentListRepository.findAll().size();
        // Create the PaymentList
        restPaymentListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentList)))
            .andExpect(status().isCreated());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentList testPaymentList = paymentListList.get(paymentListList.size() - 1);
        assertThat(testPaymentList.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testPaymentList.getSumma()).isEqualByComparingTo(DEFAULT_SUMMA);
    }

    @Test
    @Transactional
    void createPaymentListWithExistingId() throws Exception {
        // Create the PaymentList with an existing ID
        paymentList.setId(1L);

        int databaseSizeBeforeCreate = paymentListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentList)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaymentLists() throws Exception {
        // Initialize the database
        paymentListRepository.saveAndFlush(paymentList);

        // Get all the paymentListList
        restPaymentListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentList.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].summa").value(hasItem(sameNumber(DEFAULT_SUMMA))));
    }

    @Test
    @Transactional
    void getPaymentList() throws Exception {
        // Initialize the database
        paymentListRepository.saveAndFlush(paymentList);

        // Get the paymentList
        restPaymentListMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentList.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.summa").value(sameNumber(DEFAULT_SUMMA)));
    }

    @Test
    @Transactional
    void getNonExistingPaymentList() throws Exception {
        // Get the paymentList
        restPaymentListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaymentList() throws Exception {
        // Initialize the database
        paymentListRepository.saveAndFlush(paymentList);

        int databaseSizeBeforeUpdate = paymentListRepository.findAll().size();

        // Update the paymentList
        PaymentList updatedPaymentList = paymentListRepository.findById(paymentList.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentList are not directly saved in db
        em.detach(updatedPaymentList);
        updatedPaymentList.quantity(UPDATED_QUANTITY).summa(UPDATED_SUMMA);

        restPaymentListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaymentList))
            )
            .andExpect(status().isOk());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeUpdate);
        PaymentList testPaymentList = paymentListList.get(paymentListList.size() - 1);
        assertThat(testPaymentList.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPaymentList.getSumma()).isEqualTo(UPDATED_SUMMA);
    }

    @Test
    @Transactional
    void putNonExistingPaymentList() throws Exception {
        int databaseSizeBeforeUpdate = paymentListRepository.findAll().size();
        paymentList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentList() throws Exception {
        int databaseSizeBeforeUpdate = paymentListRepository.findAll().size();
        paymentList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentList() throws Exception {
        int databaseSizeBeforeUpdate = paymentListRepository.findAll().size();
        paymentList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentListWithPatch() throws Exception {
        // Initialize the database
        paymentListRepository.saveAndFlush(paymentList);

        int databaseSizeBeforeUpdate = paymentListRepository.findAll().size();

        // Update the paymentList using partial update
        PaymentList partialUpdatedPaymentList = new PaymentList();
        partialUpdatedPaymentList.setId(paymentList.getId());

        partialUpdatedPaymentList.summa(UPDATED_SUMMA);

        restPaymentListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentList))
            )
            .andExpect(status().isOk());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeUpdate);
        PaymentList testPaymentList = paymentListList.get(paymentListList.size() - 1);
        assertThat(testPaymentList.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testPaymentList.getSumma()).isEqualByComparingTo(UPDATED_SUMMA);
    }

    @Test
    @Transactional
    void fullUpdatePaymentListWithPatch() throws Exception {
        // Initialize the database
        paymentListRepository.saveAndFlush(paymentList);

        int databaseSizeBeforeUpdate = paymentListRepository.findAll().size();

        // Update the paymentList using partial update
        PaymentList partialUpdatedPaymentList = new PaymentList();
        partialUpdatedPaymentList.setId(paymentList.getId());

        partialUpdatedPaymentList.quantity(UPDATED_QUANTITY).summa(UPDATED_SUMMA);

        restPaymentListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentList))
            )
            .andExpect(status().isOk());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeUpdate);
        PaymentList testPaymentList = paymentListList.get(paymentListList.size() - 1);
        assertThat(testPaymentList.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPaymentList.getSumma()).isEqualByComparingTo(UPDATED_SUMMA);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentList() throws Exception {
        int databaseSizeBeforeUpdate = paymentListRepository.findAll().size();
        paymentList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentList() throws Exception {
        int databaseSizeBeforeUpdate = paymentListRepository.findAll().size();
        paymentList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentList))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentList() throws Exception {
        int databaseSizeBeforeUpdate = paymentListRepository.findAll().size();
        paymentList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paymentList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentList in the database
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentList() throws Exception {
        // Initialize the database
        paymentListRepository.saveAndFlush(paymentList);

        int databaseSizeBeforeDelete = paymentListRepository.findAll().size();

        // Delete the paymentList
        restPaymentListMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentList> paymentListList = paymentListRepository.findAll();
        assertThat(paymentListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
