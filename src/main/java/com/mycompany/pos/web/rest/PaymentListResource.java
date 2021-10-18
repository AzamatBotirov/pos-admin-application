package com.mycompany.pos.web.rest;

import com.mycompany.pos.domain.PaymentList;
import com.mycompany.pos.repository.PaymentListRepository;
import com.mycompany.pos.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.pos.domain.PaymentList}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PaymentListResource {

    private final Logger log = LoggerFactory.getLogger(PaymentListResource.class);

    private static final String ENTITY_NAME = "paymentList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentListRepository paymentListRepository;

    public PaymentListResource(PaymentListRepository paymentListRepository) {
        this.paymentListRepository = paymentListRepository;
    }

    /**
     * {@code POST  /payment-lists} : Create a new paymentList.
     *
     * @param paymentList the paymentList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentList, or with status {@code 400 (Bad Request)} if the paymentList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-lists")
    public ResponseEntity<PaymentList> createPaymentList(@RequestBody PaymentList paymentList) throws URISyntaxException {
        log.debug("REST request to save PaymentList : {}", paymentList);
        if (paymentList.getId() != null) {
            throw new BadRequestAlertException("A new paymentList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentList result = paymentListRepository.save(paymentList);
        return ResponseEntity
            .created(new URI("/api/payment-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-lists/:id} : Updates an existing paymentList.
     *
     * @param id the id of the paymentList to save.
     * @param paymentList the paymentList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentList,
     * or with status {@code 400 (Bad Request)} if the paymentList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-lists/{id}")
    public ResponseEntity<PaymentList> updatePaymentList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentList paymentList
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentList : {}, {}", id, paymentList);
        if (paymentList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentList result = paymentListRepository.save(paymentList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-lists/:id} : Partial updates given fields of an existing paymentList, field will ignore if it is null
     *
     * @param id the id of the paymentList to save.
     * @param paymentList the paymentList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentList,
     * or with status {@code 400 (Bad Request)} if the paymentList is not valid,
     * or with status {@code 404 (Not Found)} if the paymentList is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentList> partialUpdatePaymentList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentList paymentList
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentList partially : {}, {}", id, paymentList);
        if (paymentList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentList> result = paymentListRepository
            .findById(paymentList.getId())
            .map(existingPaymentList -> {
                if (paymentList.getQuantity() != null) {
                    existingPaymentList.setQuantity(paymentList.getQuantity());
                }
                if (paymentList.getSumma() != null) {
                    existingPaymentList.setSumma(paymentList.getSumma());
                }

                return existingPaymentList;
            })
            .map(paymentListRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentList.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-lists} : get all the paymentLists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentLists in body.
     */
    @GetMapping("/payment-lists")
    public List<PaymentList> getAllPaymentLists() {
        log.debug("REST request to get all PaymentLists");
        return paymentListRepository.findAll();
    }

    /**
     * {@code GET  /payment-lists/:id} : get the "id" paymentList.
     *
     * @param id the id of the paymentList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-lists/{id}")
    public ResponseEntity<PaymentList> getPaymentList(@PathVariable Long id) {
        log.debug("REST request to get PaymentList : {}", id);
        Optional<PaymentList> paymentList = paymentListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paymentList);
    }

    /**
     * {@code DELETE  /payment-lists/:id} : delete the "id" paymentList.
     *
     * @param id the id of the paymentList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-lists/{id}")
    public ResponseEntity<Void> deletePaymentList(@PathVariable Long id) {
        log.debug("REST request to delete PaymentList : {}", id);
        paymentListRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
