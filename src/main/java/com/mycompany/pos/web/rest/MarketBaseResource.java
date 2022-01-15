package com.mycompany.pos.web.rest;

import com.mycompany.pos.domain.MarketBase;
import com.mycompany.pos.repository.MarketBaseRepository;
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
 * REST controller for managing {@link com.mycompany.pos.domain.MarketBase}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MarketBaseResource {

    private final Logger log = LoggerFactory.getLogger(MarketBaseResource.class);

    private static final String ENTITY_NAME = "marketBase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarketBaseRepository marketBaseRepository;

    public MarketBaseResource(MarketBaseRepository marketBaseRepository) {
        this.marketBaseRepository = marketBaseRepository;
    }

    /**
     * {@code POST  /market-bases} : Create a new marketBase.
     *
     * @param marketBase the marketBase to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marketBase, or with status {@code 400 (Bad Request)} if the marketBase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/market-bases")
    public ResponseEntity<MarketBase> createMarketBase(@RequestBody MarketBase marketBase) throws URISyntaxException {
        log.debug("REST request to save MarketBase : {}", marketBase);
        if (marketBase.getId() != null) {
            throw new BadRequestAlertException("A new marketBase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MarketBase result = marketBaseRepository.save(marketBase);
        return ResponseEntity
            .created(new URI("/api/market-bases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /market-bases/:id} : Updates an existing marketBase.
     *
     * @param id the id of the marketBase to save.
     * @param marketBase the marketBase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketBase,
     * or with status {@code 400 (Bad Request)} if the marketBase is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marketBase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/market-bases/{id}")
    public ResponseEntity<MarketBase> updateMarketBase(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MarketBase marketBase
    ) throws URISyntaxException {
        log.debug("REST request to update MarketBase : {}, {}", id, marketBase);
        if (marketBase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketBase.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketBaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MarketBase result = marketBaseRepository.save(marketBase);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marketBase.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /market-bases/:id} : Partial updates given fields of an existing marketBase, field will ignore if it is null
     *
     * @param id the id of the marketBase to save.
     * @param marketBase the marketBase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketBase,
     * or with status {@code 400 (Bad Request)} if the marketBase is not valid,
     * or with status {@code 404 (Not Found)} if the marketBase is not found,
     * or with status {@code 500 (Internal Server Error)} if the marketBase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/market-bases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MarketBase> partialUpdateMarketBase(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MarketBase marketBase
    ) throws URISyntaxException {
        log.debug("REST request to partial update MarketBase partially : {}, {}", id, marketBase);
        if (marketBase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketBase.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketBaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MarketBase> result = marketBaseRepository
            .findById(marketBase.getId())
            .map(existingMarketBase -> {
                if (marketBase.getQuantity() != null) {
                    existingMarketBase.setQuantity(marketBase.getQuantity());
                }
                if (marketBase.getPrice() != null) {
                    existingMarketBase.setPrice(marketBase.getPrice());
                }
                if (marketBase.getCurrentPrice() != null) {
                    existingMarketBase.setCurrentPrice(marketBase.getCurrentPrice());
                }
                if (marketBase.getCreateDate() != null) {
                    existingMarketBase.setCreateDate(marketBase.getCreateDate());
                }
                if (marketBase.getDate() != null) {
                    existingMarketBase.setDate(marketBase.getDate());
                }

                return existingMarketBase;
            })
            .map(marketBaseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marketBase.getId().toString())
        );
    }

    /**
     * {@code GET  /market-bases} : get all the marketBases.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marketBases in body.
     */
    @GetMapping("/market-bases")
    public List<MarketBase> getAllMarketBases() {
        log.debug("REST request to get all MarketBases");
        return marketBaseRepository.findAll();
    }

    /**
     * {@code GET  /market-bases/:id} : get the "id" marketBase.
     *
     * @param id the id of the marketBase to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marketBase, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/market-bases/{id}")
    public ResponseEntity<MarketBase> getMarketBase(@PathVariable Long id) {
        log.debug("REST request to get MarketBase : {}", id);
        Optional<MarketBase> marketBase = marketBaseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(marketBase);
    }

    /**
     * {@code DELETE  /market-bases/:id} : delete the "id" marketBase.
     *
     * @param id the id of the marketBase to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/market-bases/{id}")
    public ResponseEntity<Void> deleteMarketBase(@PathVariable Long id) {
        log.debug("REST request to delete MarketBase : {}", id);
        marketBaseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
