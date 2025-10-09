package com.blitz.account.service;

import com.blitz.account.domain.*; // for static metamodels
import com.blitz.account.domain.FleetTrip;
import com.blitz.account.repository.FleetTripRepository;
import com.blitz.account.service.criteria.FleetTripCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FleetTrip} entities in the database.
 * The main input is a {@link FleetTripCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FleetTrip} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FleetTripQueryService extends QueryService<FleetTrip> {

    private static final Logger LOG = LoggerFactory.getLogger(FleetTripQueryService.class);

    private final FleetTripRepository fleetTripRepository;

    public FleetTripQueryService(FleetTripRepository fleetTripRepository) {
        this.fleetTripRepository = fleetTripRepository;
    }

    /**
     * Return a {@link Page} of {@link FleetTrip} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FleetTrip> findByCriteria(FleetTripCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FleetTrip> specification = createSpecification(criteria);
        return fleetTripRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FleetTripCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FleetTrip> specification = createSpecification(criteria);
        return fleetTripRepository.count(specification);
    }

    /**
     * Function to convert {@link FleetTripCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FleetTrip> createSpecification(FleetTripCriteria criteria) {
        Specification<FleetTrip> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), FleetTrip_.id),
                buildRangeSpecification(criteria.getVehicleId(), FleetTrip_.vehicleId),
                buildRangeSpecification(criteria.getDriverId(), FleetTrip_.driverId),
                buildRangeSpecification(criteria.getStartDate(), FleetTrip_.startDate),
                buildRangeSpecification(criteria.getEndDate(), FleetTrip_.endDate),
                buildRangeSpecification(criteria.getDistanceKm(), FleetTrip_.distanceKm),
                buildStringSpecification(criteria.getStartLocation(), FleetTrip_.startLocation),
                buildStringSpecification(criteria.getEndLocation(), FleetTrip_.endLocation),
                buildStringSpecification(criteria.getLoadType(), FleetTrip_.loadType),
                buildStringSpecification(criteria.getLoadDescription(), FleetTrip_.loadDescription),
                buildRangeSpecification(criteria.getTripCost(), FleetTrip_.tripCost)
            );
        }
        return specification;
    }
}
