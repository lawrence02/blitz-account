package com.blitz.account.domain;

import com.blitz.account.domain.enumeration.IncidentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IncidentLog.
 */
@Entity
@Table(name = "incident_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncidentLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "trip_id")
    private Long tripId;

    @NotNull
    @Column(name = "incident_date", nullable = false)
    private Instant incidentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private IncidentType type;

    @Column(name = "description")
    private String description;

    @Column(name = "cost", precision = 21, scale = 2)
    private BigDecimal cost;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IncidentLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public IncidentLog vehicleId(Long vehicleId) {
        this.setVehicleId(vehicleId);
        return this;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getTripId() {
        return this.tripId;
    }

    public IncidentLog tripId(Long tripId) {
        this.setTripId(tripId);
        return this;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Instant getIncidentDate() {
        return this.incidentDate;
    }

    public IncidentLog incidentDate(Instant incidentDate) {
        this.setIncidentDate(incidentDate);
        return this;
    }

    public void setIncidentDate(Instant incidentDate) {
        this.incidentDate = incidentDate;
    }

    public IncidentType getType() {
        return this.type;
    }

    public IncidentLog type(IncidentType type) {
        this.setType(type);
        return this;
    }

    public void setType(IncidentType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public IncidentLog description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public IncidentLog cost(BigDecimal cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncidentLog)) {
            return false;
        }
        return getId() != null && getId().equals(((IncidentLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncidentLog{" +
            "id=" + getId() +
            ", vehicleId=" + getVehicleId() +
            ", tripId=" + getTripId() +
            ", incidentDate='" + getIncidentDate() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", cost=" + getCost() +
            "}";
    }
}
