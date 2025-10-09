package com.blitz.account.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FleetTripLocation.
 */
@Entity
@Table(name = "fleet_trip_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FleetTripLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fleet_trip_id", nullable = false)
    private Long fleetTripId;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "heading")
    private Double heading;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FleetTripLocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFleetTripId() {
        return this.fleetTripId;
    }

    public FleetTripLocation fleetTripId(Long fleetTripId) {
        this.setFleetTripId(fleetTripId);
        return this;
    }

    public void setFleetTripId(Long fleetTripId) {
        this.fleetTripId = fleetTripId;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public FleetTripLocation timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public FleetTripLocation latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public FleetTripLocation longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSpeed() {
        return this.speed;
    }

    public FleetTripLocation speed(Double speed) {
        this.setSpeed(speed);
        return this;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getHeading() {
        return this.heading;
    }

    public FleetTripLocation heading(Double heading) {
        this.setHeading(heading);
        return this;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FleetTripLocation)) {
            return false;
        }
        return getId() != null && getId().equals(((FleetTripLocation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FleetTripLocation{" +
            "id=" + getId() +
            ", fleetTripId=" + getFleetTripId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", speed=" + getSpeed() +
            ", heading=" + getHeading() +
            "}";
    }
}
