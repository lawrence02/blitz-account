package com.blitz.account.web.rest;

import com.blitz.account.domain.FleetTripLocation;
import com.blitz.account.repository.FleetTripLocationRepository;
import com.blitz.account.service.FleetTripLocationService;
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
 * REST controller for managing {@link com.blitz.account.domain.FleetTripLocation}.
 */
@RestController
@RequestMapping("/api/fleet-trip-locations")
public class FleetTripLocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(FleetTripLocationResource.class);

    private static final String ENTITY_NAME = "fleetTripLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FleetTripLocationService fleetTripLocationService;

    private final FleetTripLocationRepository fleetTripLocationRepository;

    public FleetTripLocationResource(
        FleetTripLocationService fleetTripLocationService,
        FleetTripLocationRepository fleetTripLocationRepository
    ) {
        this.fleetTripLocationService = fleetTripLocationService;
        this.fleetTripLocationRepository = fleetTripLocationRepository;
    }

    /**
     * {@code POST  /fleet-trip-locations} : Create a new fleetTripLocation.
     *
     * @param fleetTripLocation the fleetTripLocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fleetTripLocation, or with status {@code 400 (Bad Request)} if the fleetTripLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FleetTripLocation> createFleetTripLocation(@Valid @RequestBody FleetTripLocation fleetTripLocation)
        throws URISyntaxException {
        LOG.debug("REST request to save FleetTripLocation : {}", fleetTripLocation);
        if (fleetTripLocation.getId() != null) {
            throw new BadRequestAlertException("A new fleetTripLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fleetTripLocation = fleetTripLocationService.save(fleetTripLocation);
        return ResponseEntity.created(new URI("/api/fleet-trip-locations/" + fleetTripLocation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fleetTripLocation.getId().toString()))
            .body(fleetTripLocation);
    }

    /**
     * {@code PUT  /fleet-trip-locations/:id} : Updates an existing fleetTripLocation.
     *
     * @param id the id of the fleetTripLocation to save.
     * @param fleetTripLocation the fleetTripLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fleetTripLocation,
     * or with status {@code 400 (Bad Request)} if the fleetTripLocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fleetTripLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FleetTripLocation> updateFleetTripLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FleetTripLocation fleetTripLocation
    ) throws URISyntaxException {
        LOG.debug("REST request to update FleetTripLocation : {}, {}", id, fleetTripLocation);
        if (fleetTripLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fleetTripLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fleetTripLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fleetTripLocation = fleetTripLocationService.update(fleetTripLocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fleetTripLocation.getId().toString()))
            .body(fleetTripLocation);
    }

    /**
     * {@code PATCH  /fleet-trip-locations/:id} : Partial updates given fields of an existing fleetTripLocation, field will ignore if it is null
     *
     * @param id the id of the fleetTripLocation to save.
     * @param fleetTripLocation the fleetTripLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fleetTripLocation,
     * or with status {@code 400 (Bad Request)} if the fleetTripLocation is not valid,
     * or with status {@code 404 (Not Found)} if the fleetTripLocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the fleetTripLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FleetTripLocation> partialUpdateFleetTripLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FleetTripLocation fleetTripLocation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FleetTripLocation partially : {}, {}", id, fleetTripLocation);
        if (fleetTripLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fleetTripLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fleetTripLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FleetTripLocation> result = fleetTripLocationService.partialUpdate(fleetTripLocation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fleetTripLocation.getId().toString())
        );
    }

    /**
     * {@code GET  /fleet-trip-locations} : get all the fleetTripLocations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fleetTripLocations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FleetTripLocation>> getAllFleetTripLocations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of FleetTripLocations");
        Page<FleetTripLocation> page = fleetTripLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fleet-trip-locations/:id} : get the "id" fleetTripLocation.
     *
     * @param id the id of the fleetTripLocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fleetTripLocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FleetTripLocation> getFleetTripLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FleetTripLocation : {}", id);
        Optional<FleetTripLocation> fleetTripLocation = fleetTripLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fleetTripLocation);
    }

    /**
     * {@code DELETE  /fleet-trip-locations/:id} : delete the "id" fleetTripLocation.
     *
     * @param id the id of the fleetTripLocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFleetTripLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FleetTripLocation : {}", id);
        fleetTripLocationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
