package com.parking.components.parkingfee;

import com.parking.constants.ParkingFeeType;
import com.parking.model.ParkingFeeHelper;

public interface IParkingFee {
    ParkingFeeType getType();

    Double calculateParkingFee(final ParkingFeeHelper feeHelper);
}
