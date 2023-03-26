package com.parking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "parking_spots")
public class ParkingSpots {
    private String vehicleType;
    private Integer spotNumber;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public ParkingSpots(String vehicleType, Integer spotNumber) {
        this.vehicleType = vehicleType;
        this.spotNumber = spotNumber;
    }

    public ParkingSpots() {
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Integer getSpotNumber() {
        return spotNumber;
    }
}
