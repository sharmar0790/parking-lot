package com.parking.repository;

public interface ParkingRepositoryCustom {
    int getAndReduceAvailableSlotsByVehicleType(final String vehicleType);

    void updateAvailableSlotsByVehicleType(final String vehicleType);
}
