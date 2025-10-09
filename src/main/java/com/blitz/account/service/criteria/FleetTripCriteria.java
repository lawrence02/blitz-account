package com.blitz.account.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.blitz.account.domain.FleetTrip} entity. This class is used
 * in {@link com.blitz.account.web.rest.FleetTripResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fleet-trips?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FleetTripCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter vehicleId;

    private LongFilter driverId;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private DoubleFilter distanceKm;

    private StringFilter startLocation;

    private StringFilter endLocation;

    private StringFilter loadType;

    private StringFilter loadDescription;

    private BigDecimalFilter tripCost;

    private Boolean distinct;

    public FleetTripCriteria() {}

    public FleetTripCriteria(FleetTripCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.vehicleId = other.optionalVehicleId().map(LongFilter::copy).orElse(null);
        this.driverId = other.optionalDriverId().map(LongFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.distanceKm = other.optionalDistanceKm().map(DoubleFilter::copy).orElse(null);
        this.startLocation = other.optionalStartLocation().map(StringFilter::copy).orElse(null);
        this.endLocation = other.optionalEndLocation().map(StringFilter::copy).orElse(null);
        this.loadType = other.optionalLoadType().map(StringFilter::copy).orElse(null);
        this.loadDescription = other.optionalLoadDescription().map(StringFilter::copy).orElse(null);
        this.tripCost = other.optionalTripCost().map(BigDecimalFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FleetTripCriteria copy() {
        return new FleetTripCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getVehicleId() {
        return vehicleId;
    }

    public Optional<LongFilter> optionalVehicleId() {
        return Optional.ofNullable(vehicleId);
    }

    public LongFilter vehicleId() {
        if (vehicleId == null) {
            setVehicleId(new LongFilter());
        }
        return vehicleId;
    }

    public void setVehicleId(LongFilter vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LongFilter getDriverId() {
        return driverId;
    }

    public Optional<LongFilter> optionalDriverId() {
        return Optional.ofNullable(driverId);
    }

    public LongFilter driverId() {
        if (driverId == null) {
            setDriverId(new LongFilter());
        }
        return driverId;
    }

    public void setDriverId(LongFilter driverId) {
        this.driverId = driverId;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public DoubleFilter getDistanceKm() {
        return distanceKm;
    }

    public Optional<DoubleFilter> optionalDistanceKm() {
        return Optional.ofNullable(distanceKm);
    }

    public DoubleFilter distanceKm() {
        if (distanceKm == null) {
            setDistanceKm(new DoubleFilter());
        }
        return distanceKm;
    }

    public void setDistanceKm(DoubleFilter distanceKm) {
        this.distanceKm = distanceKm;
    }

    public StringFilter getStartLocation() {
        return startLocation;
    }

    public Optional<StringFilter> optionalStartLocation() {
        return Optional.ofNullable(startLocation);
    }

    public StringFilter startLocation() {
        if (startLocation == null) {
            setStartLocation(new StringFilter());
        }
        return startLocation;
    }

    public void setStartLocation(StringFilter startLocation) {
        this.startLocation = startLocation;
    }

    public StringFilter getEndLocation() {
        return endLocation;
    }

    public Optional<StringFilter> optionalEndLocation() {
        return Optional.ofNullable(endLocation);
    }

    public StringFilter endLocation() {
        if (endLocation == null) {
            setEndLocation(new StringFilter());
        }
        return endLocation;
    }

    public void setEndLocation(StringFilter endLocation) {
        this.endLocation = endLocation;
    }

    public StringFilter getLoadType() {
        return loadType;
    }

    public Optional<StringFilter> optionalLoadType() {
        return Optional.ofNullable(loadType);
    }

    public StringFilter loadType() {
        if (loadType == null) {
            setLoadType(new StringFilter());
        }
        return loadType;
    }

    public void setLoadType(StringFilter loadType) {
        this.loadType = loadType;
    }

    public StringFilter getLoadDescription() {
        return loadDescription;
    }

    public Optional<StringFilter> optionalLoadDescription() {
        return Optional.ofNullable(loadDescription);
    }

    public StringFilter loadDescription() {
        if (loadDescription == null) {
            setLoadDescription(new StringFilter());
        }
        return loadDescription;
    }

    public void setLoadDescription(StringFilter loadDescription) {
        this.loadDescription = loadDescription;
    }

    public BigDecimalFilter getTripCost() {
        return tripCost;
    }

    public Optional<BigDecimalFilter> optionalTripCost() {
        return Optional.ofNullable(tripCost);
    }

    public BigDecimalFilter tripCost() {
        if (tripCost == null) {
            setTripCost(new BigDecimalFilter());
        }
        return tripCost;
    }

    public void setTripCost(BigDecimalFilter tripCost) {
        this.tripCost = tripCost;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FleetTripCriteria that = (FleetTripCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(vehicleId, that.vehicleId) &&
            Objects.equals(driverId, that.driverId) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(distanceKm, that.distanceKm) &&
            Objects.equals(startLocation, that.startLocation) &&
            Objects.equals(endLocation, that.endLocation) &&
            Objects.equals(loadType, that.loadType) &&
            Objects.equals(loadDescription, that.loadDescription) &&
            Objects.equals(tripCost, that.tripCost) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            vehicleId,
            driverId,
            startDate,
            endDate,
            distanceKm,
            startLocation,
            endLocation,
            loadType,
            loadDescription,
            tripCost,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FleetTripCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalVehicleId().map(f -> "vehicleId=" + f + ", ").orElse("") +
            optionalDriverId().map(f -> "driverId=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalDistanceKm().map(f -> "distanceKm=" + f + ", ").orElse("") +
            optionalStartLocation().map(f -> "startLocation=" + f + ", ").orElse("") +
            optionalEndLocation().map(f -> "endLocation=" + f + ", ").orElse("") +
            optionalLoadType().map(f -> "loadType=" + f + ", ").orElse("") +
            optionalLoadDescription().map(f -> "loadDescription=" + f + ", ").orElse("") +
            optionalTripCost().map(f -> "tripCost=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
