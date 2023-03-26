package com.parking.repository.impl;

import com.parking.repository.ParkingRepositoryCustom;
import com.parking.service.CacheService;

public class ParkingRepositoryImpl implements ParkingRepositoryCustom {

    private final CacheService cacheService;

    public ParkingRepositoryImpl(final CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public int getAndReduceAvailableSlotsByVehicleType(final String vehicleType) {
        Integer nextSlot = this.cacheService.getAvailableSlotsByVehicle(vehicleType).get();
        this.cacheService.reduceAvailableSlotsByVehicle(vehicleType);
        return nextSlot;

    }

    @Override
    public void updateAvailableSlotsByVehicleType(String vehicleType) {
        this.cacheService.updateAvailableSlotsByVehicleType(vehicleType);
    }
}
