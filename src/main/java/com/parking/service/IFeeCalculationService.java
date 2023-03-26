package com.parking.service;

import com.parking.model.ParkingTicket;

import java.time.LocalDateTime;

public interface IFeeCalculationService {

    Double calculateParkFee(final ParkingTicket parkVehicleDetail, final LocalDateTime parkingEndTime);
}
