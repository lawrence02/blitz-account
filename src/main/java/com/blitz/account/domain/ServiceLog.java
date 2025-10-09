package com.blitz.account.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServiceLog.
 */
@Entity
@Table(name = "service_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @NotNull
    @Column(name = "service_date", nullable = false)
    private Instant serviceDate;

    @Column(name = "description")
    private String description;

    @Column(name = "cost", precision = 21, scale = 2)
    private BigDecimal cost;

    @Column(name = "mileage_at_service")
    private Double mileageAtService;

    @Column(name = "supplier_id")
    private Long supplierId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public ServiceLog vehicleId(Long vehicleId) {
        this.setVehicleId(vehicleId);
        return this;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Instant getServiceDate() {
        return this.serviceDate;
    }

    public ServiceLog serviceDate(Instant serviceDate) {
        this.setServiceDate(serviceDate);
        return this;
    }

    public void setServiceDate(Instant serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getDescription() {
        return this.description;
    }

    public ServiceLog description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public ServiceLog cost(BigDecimal cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Double getMileageAtService() {
        return this.mileageAtService;
    }

    public ServiceLog mileageAtService(Double mileageAtService) {
        this.setMileageAtService(mileageAtService);
        return this;
    }

    public void setMileageAtService(Double mileageAtService) {
        this.mileageAtService = mileageAtService;
    }

    public Long getSupplierId() {
        return this.supplierId;
    }

    public ServiceLog supplierId(Long supplierId) {
        this.setSupplierId(supplierId);
        return this;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceLog)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceLog{" +
            "id=" + getId() +
            ", vehicleId=" + getVehicleId() +
            ", serviceDate='" + getServiceDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", cost=" + getCost() +
            ", mileageAtService=" + getMileageAtService() +
            ", supplierId=" + getSupplierId() +
            "}";
    }
}
