package com.blitz.account.service.dto;

public class VehicleStatsDTO {

    private int available;
    private int inTrip;
    private int maintenance;
    private int idle;
    private int total;

    public VehicleStatsDTO() {}

    public VehicleStatsDTO(int available, int inTrip, int maintenance, int idle) {
        this.available = available;
        this.inTrip = inTrip;
        this.maintenance = maintenance;
        this.idle = idle;
        this.total = available + inTrip + maintenance + idle;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getInTrip() {
        return inTrip;
    }

    public void setInTrip(int inTrip) {
        this.inTrip = inTrip;
    }

    public int getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(int maintenance) {
        this.maintenance = maintenance;
    }

    public int getIdle() {
        return idle;
    }

    public void setIdle(int idle) {
        this.idle = idle;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return (
            "VehicleStatsDTO [available=" +
            available +
            ", inTrip=" +
            inTrip +
            ", maintenance=" +
            maintenance +
            ", idle=" +
            idle +
            ", total=" +
            total +
            "]"
        );
    }
}
