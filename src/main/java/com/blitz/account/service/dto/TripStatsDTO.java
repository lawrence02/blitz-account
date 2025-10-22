package com.blitz.account.service.dto;

public class TripStatsDTO {

    private long activeTrips;
    private long completedToday;
    private long scheduledTomorrow;

    public TripStatsDTO() {}

    public TripStatsDTO(long activeTrips, long completedToday, long scheduledTomorrow) {
        this.activeTrips = activeTrips;
        this.completedToday = completedToday;
        this.scheduledTomorrow = scheduledTomorrow;
    }

    public long getActiveTrips() {
        return activeTrips;
    }

    public void setActiveTrips(long activeTrips) {
        this.activeTrips = activeTrips;
    }

    public long getCompletedToday() {
        return completedToday;
    }

    public void setCompletedToday(long completedToday) {
        this.completedToday = completedToday;
    }

    public long getScheduledTomorrow() {
        return scheduledTomorrow;
    }

    public void setScheduledTomorrow(long scheduledTomorrow) {
        this.scheduledTomorrow = scheduledTomorrow;
    }

    @Override
    public String toString() {
        return (
            "TripStatsDTO [activeTrips=" +
            activeTrips +
            ", completedToday=" +
            completedToday +
            ", scheduledTomorrow=" +
            scheduledTomorrow +
            "]"
        );
    }
}
