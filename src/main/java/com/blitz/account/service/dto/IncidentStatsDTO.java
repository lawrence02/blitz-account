package com.blitz.account.service.dto;

public class IncidentStatsDTO {

    private int accidents;
    private int breakdowns;
    private int dents;
    private int total;
    private int thisMonth;

    public IncidentStatsDTO() {}

    public IncidentStatsDTO(int accidents, int breakdowns, int dents, int thisMonth) {
        this.accidents = accidents;
        this.breakdowns = breakdowns;
        this.dents = dents;
        this.thisMonth = thisMonth;
        this.total = accidents + breakdowns + dents;
    }

    public int getAccidents() {
        return accidents;
    }

    public void setAccidents(int accidents) {
        this.accidents = accidents;
    }

    public int getBreakdowns() {
        return breakdowns;
    }

    public void setBreakdowns(int breakdowns) {
        this.breakdowns = breakdowns;
    }

    public int getDents() {
        return dents;
    }

    public void setDents(int dents) {
        this.dents = dents;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(int thisMonth) {
        this.thisMonth = thisMonth;
    }

    @Override
    public String toString() {
        return (
            "IncidentStatsDTO [accidents=" +
            accidents +
            ", breakdowns=" +
            breakdowns +
            ", dents=" +
            dents +
            ", total=" +
            total +
            ", thisMonth=" +
            thisMonth +
            "]"
        );
    }
}
