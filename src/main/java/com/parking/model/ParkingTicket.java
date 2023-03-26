package com.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "parking_ticket")
public class ParkingTicket implements Serializable {

    private String ticketNumber;
    @JsonIgnore
    private String vehicleType;
    private String spotNumber;
    @JsonIgnore
    private String location;
    private LocalDateTime entryDateTime;
    private LocalDateTime exitDateTime;
    @Id
    @JsonIgnore
    @GeneratedValue(generator = "parking_ticket_seq")
    @GenericGenerator(
            name = "parking_ticket_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "parking_ticket_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1"),
                    @Parameter(name = "allocation_size", value = "50")
            }
    )
    private Integer id;

    public ParkingTicket() {
    }

    public ParkingTicket(final String ticketNumber, final String vehicleType, final String spotNumber,
                         final String location, final LocalDateTime entryDateTime,
                         final LocalDateTime exitDateTime) {
        this.ticketNumber = ticketNumber;
        this.vehicleType = vehicleType;
        this.spotNumber = spotNumber;
        this.location = location;
        this.entryDateTime = entryDateTime;
        this.exitDateTime = exitDateTime;
    }

    public Integer getId() {
        return id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getEntryDateTime() {
        return entryDateTime;
    }

    public LocalDateTime getExitDateTime() {
        return exitDateTime;
    }

    public void setExitDateTime(LocalDateTime exitDateTime) {
        this.exitDateTime = exitDateTime;
    }
}
