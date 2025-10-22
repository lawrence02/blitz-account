package com.blitz.account.web.rest;

import com.blitz.account.domain.FleetTrip;
import com.blitz.account.repository.FleetTripRepository;
import com.blitz.account.service.FleetTripQueryService;
import com.blitz.account.service.FleetTripService;
import com.blitz.account.service.criteria.FleetTripCriteria;
import com.blitz.account.service.dto.TripStatsDTO;
import com.blitz.account.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.blitz.account.domain.FleetTrip}.
 */
@RestController
@RequestMapping("/api/fleet-trips")
public class FleetTripResource {

    private static final Logger LOG = LoggerFactory.getLogger(FleetTripResource.class);

    private static final String ENTITY_NAME = "fleetTrip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FleetTripService fleetTripService;

    private final FleetTripRepository fleetTripRepository;

    private final FleetTripQueryService fleetTripQueryService;

    public FleetTripResource(
        FleetTripService fleetTripService,
        FleetTripRepository fleetTripRepository,
        FleetTripQueryService fleetTripQueryService
    ) {
        this.fleetTripService = fleetTripService;
        this.fleetTripRepository = fleetTripRepository;
        this.fleetTripQueryService = fleetTripQueryService;
    }

    /**
     * {@code POST  /fleet-trips} : Create a new fleetTrip.
     *
     * @param fleetTrip the fleetTrip to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fleetTrip, or with status {@code 400 (Bad Request)} if the fleetTrip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FleetTrip> createFleetTrip(@Valid @RequestBody FleetTrip fleetTrip) throws URISyntaxException {
        LOG.debug("REST request to save FleetTrip : {}", fleetTrip);
        if (fleetTrip.getId() != null) {
            throw new BadRequestAlertException("A new fleetTrip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fleetTrip = fleetTripService.save(fleetTrip);
        return ResponseEntity.created(new URI("/api/fleet-trips/" + fleetTrip.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fleetTrip.getId().toString()))
            .body(fleetTrip);
    }

    /**
     * {@code PUT  /fleet-trips/:id} : Updates an existing fleetTrip.
     *
     * @param id the id of the fleetTrip to save.
     * @param fleetTrip the fleetTrip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fleetTrip,
     * or with status {@code 400 (Bad Request)} if the fleetTrip is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fleetTrip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FleetTrip> updateFleetTrip(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FleetTrip fleetTrip
    ) throws URISyntaxException {
        LOG.debug("REST request to update FleetTrip : {}, {}", id, fleetTrip);
        if (fleetTrip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fleetTrip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fleetTripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fleetTrip = fleetTripService.update(fleetTrip);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fleetTrip.getId().toString()))
            .body(fleetTrip);
    }

    /**
     * {@code PATCH  /fleet-trips/:id} : Partial updates given fields of an existing fleetTrip, field will ignore if it is null
     *
     * @param id the id of the fleetTrip to save.
     * @param fleetTrip the fleetTrip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fleetTrip,
     * or with status {@code 400 (Bad Request)} if the fleetTrip is not valid,
     * or with status {@code 404 (Not Found)} if the fleetTrip is not found,
     * or with status {@code 500 (Internal Server Error)} if the fleetTrip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FleetTrip> partialUpdateFleetTrip(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FleetTrip fleetTrip
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FleetTrip partially : {}, {}", id, fleetTrip);
        if (fleetTrip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fleetTrip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fleetTripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FleetTrip> result = fleetTripService.partialUpdate(fleetTrip);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fleetTrip.getId().toString())
        );
    }

    /**
     * {@code GET  /fleet-trips} : get all the fleetTrips.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fleetTrips in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FleetTrip>> getAllFleetTrips(
        FleetTripCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FleetTrips by criteria: {}", criteria);

        Page<FleetTrip> page = fleetTripQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fleet-trips/count} : count all the fleetTrips.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFleetTrips(FleetTripCriteria criteria) {
        LOG.debug("REST request to count FleetTrips by criteria: {}", criteria);
        return ResponseEntity.ok().body(fleetTripQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fleet-trips/:id} : get the "id" fleetTrip.
     *
     * @param id the id of the fleetTrip to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fleetTrip, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FleetTrip> getFleetTrip(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FleetTrip : {}", id);
        Optional<FleetTrip> fleetTrip = fleetTripService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fleetTrip);
    }

    /**
     * {@code DELETE  /fleet-trips/:id} : delete the "id" fleetTrip.
     *
     * @param id the id of the fleetTrip to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFleetTrip(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FleetTrip : {}", id);
        fleetTripService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/trip-stats")
    public TripStatsDTO getTripStats() {
        return fleetTripService.getTripStats();
    }
}
