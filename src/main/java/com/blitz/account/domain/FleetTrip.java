package com.blitz.account.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FleetTrip.
 */
@Entity
@Table(name = "fleet_trip")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FleetTrip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "driver_id")
    private Long driverId;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "start_location")
    private String startLocation;

    @Column(name = "end_location")
    private String endLocation;

    @Column(name = "load_type")
    private String loadType;

    @Column(name = "load_description")
    private String loadDescription;

    @Lob
    @Column(name = "route_geo_coordinates")
    private String routeGeoCoordinates;

    @Column(name = "trip_cost", precision = 21, scale = 2)
    private BigDecimal tripCost;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FleetTrip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public FleetTrip vehicleId(Long vehicleId) {
        this.setVehicleId(vehicleId);
        return this;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getDriverId() {
        return this.driverId;
    }

    public FleetTrip driverId(Long driverId) {
        this.setDriverId(driverId);
        return this;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public FleetTrip startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public FleetTrip endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Double getDistanceKm() {
        return this.distanceKm;
    }

    public FleetTrip distanceKm(Double distanceKm) {
        this.setDistanceKm(distanceKm);
        return this;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public FleetTrip startLocation(String startLocation) {
        this.setStartLocation(startLocation);
        return this;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return this.endLocation;
    }

    public FleetTrip endLocation(String endLocation) {
        this.setEndLocation(endLocation);
        return this;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getLoadType() {
        return this.loadType;
    }

    public FleetTrip loadType(String loadType) {
        this.setLoadType(loadType);
        return this;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public String getLoadDescription() {
        return this.loadDescription;
    }

    public FleetTrip loadDescription(String loadDescription) {
        this.setLoadDescription(loadDescription);
        return this;
    }

    public void setLoadDescription(String loadDescription) {
        this.loadDescription = loadDescription;
    }

    public String getRouteGeoCoordinates() {
        return this.routeGeoCoordinates;
    }

    public FleetTrip routeGeoCoordinates(String routeGeoCoordinates) {
        this.setRouteGeoCoordinates(routeGeoCoordinates);
        return this;
    }

    public void setRouteGeoCoordinates(String routeGeoCoordinates) {
        this.routeGeoCoordinates = routeGeoCoordinates;
    }

    public BigDecimal getTripCost() {
        return this.tripCost;
    }

    public FleetTrip tripCost(BigDecimal tripCost) {
        this.setTripCost(tripCost);
        return this;
    }

    public void setTripCost(BigDecimal tripCost) {
        this.tripCost = tripCost;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FleetTrip)) {
            return false;
        }
        return getId() != null && getId().equals(((FleetTrip) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FleetTrip{" +
            "id=" + getId() +
            ", vehicleId=" + getVehicleId() +
            ", driverId=" + getDriverId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", distanceKm=" + getDistanceKm() +
            ", startLocation='" + getStartLocation() + "'" +
            ", endLocation='" + getEndLocation() + "'" +
            ", loadType='" + getLoadType() + "'" +
            ", loadDescription='" + getLoadDescription() + "'" +
            ", routeGeoCoordinates='" + getRouteGeoCoordinates() + "'" +
            ", tripCost=" + getTripCost() +
            "}";
    }
}
