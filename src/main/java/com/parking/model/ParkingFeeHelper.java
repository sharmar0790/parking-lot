package com.parking.model;

import java.time.LocalDateTime;
import java.util.List;

public class ParkingFeeHelper {
    private final List<IntervalFeeRates> rates;
    private final LocalDateTime entryDateTime;
    private final LocalDateTime exitDateTime;

    public ParkingFeeHelper(final List<IntervalFeeRates> intervalRates,
                            final LocalDateTime entryDateTime,
                            final LocalDateTime exitDateTime) {
        this.rates = intervalRates;
        this.entryDateTime = entryDateTime;
        this.exitDateTime = exitDateTime;
    }

    public List<IntervalFeeRates> getRates() {
        return rates;
    }

    public LocalDateTime getEntryDateTime() {
        return entryDateTime;
    }

    public LocalDateTime getExitDateTime() {
        return exitDateTime;
    }

}
