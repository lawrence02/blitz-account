package com.blitz.account.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FuelLog.
 */
@Entity
@Table(name = "fuel_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FuelLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "fuel_volume")
    private Double fuelVolume;

    @Column(name = "fuel_cost", precision = 21, scale = 2)
    private BigDecimal fuelCost;

    @Column(name = "location")
    private String location;

    @Column(name = "trip_id")
    private Long tripId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FuelLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public FuelLog vehicleId(Long vehicleId) {
        this.setVehicleId(vehicleId);
        return this;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Instant getDate() {
        return this.date;
    }

    public FuelLog date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getFuelVolume() {
        return this.fuelVolume;
    }

    public FuelLog fuelVolume(Double fuelVolume) {
        this.setFuelVolume(fuelVolume);
        return this;
    }

    public void setFuelVolume(Double fuelVolume) {
        this.fuelVolume = fuelVolume;
    }

    public BigDecimal getFuelCost() {
        return this.fuelCost;
    }

    public FuelLog fuelCost(BigDecimal fuelCost) {
        this.setFuelCost(fuelCost);
        return this;
    }

    public void setFuelCost(BigDecimal fuelCost) {
        this.fuelCost = fuelCost;
    }

    public String getLocation() {
        return this.location;
    }

    public FuelLog location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getTripId() {
        return this.tripId;
    }

    public FuelLog tripId(Long tripId) {
        this.setTripId(tripId);
        return this;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FuelLog)) {
            return false;
        }
        return getId() != null && getId().equals(((FuelLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FuelLog{" +
            "id=" + getId() +
            ", vehicleId=" + getVehicleId() +
            ", date='" + getDate() + "'" +
            ", fuelVolume=" + getFuelVolume() +
            ", fuelCost=" + getFuelCost() +
            ", location='" + getLocation() + "'" +
            ", tripId=" + getTripId() +
            "}";
    }
}
