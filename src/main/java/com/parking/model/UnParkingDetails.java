package com.parking.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UnParkingDetails implements Serializable {

    private final String receiptNumber;
    private final LocalDateTime entryDateTime;
    private final LocalDateTime exitDateTime;
    private final Double fee;

    public UnParkingDetails(final String receiptNumber, final Double fee, final LocalDateTime entryDateTime,
                            final LocalDateTime exitDateTime) {
        this.receiptNumber = receiptNumber;
        this.fee = fee;
        this.entryDateTime = entryDateTime;
        this.exitDateTime = exitDateTime;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public LocalDateTime getEntryDateTime() {
        return entryDateTime;
    }

    public LocalDateTime getExitDateTime() {
        return exitDateTime;
    }

    public Double getFee() {
        return fee;
    }

}
