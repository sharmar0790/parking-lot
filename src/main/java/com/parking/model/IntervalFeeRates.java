package com.parking.model;

public class IntervalFeeRates {
    private Integer startHour;
    private Integer endHour;
    private Double rate;

    public IntervalFeeRates() {
    }

    public IntervalFeeRates(Integer startHour, Integer endHour, Double rate) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.rate = rate;
    }

    public IntervalFeeRates(Double rate) {
        this.rate = rate;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public Double getRate() {
        return rate;
    }
}
