package com.parking.components.feemodel;

import com.parking.constants.FeeModelType;
import com.parking.model.IntervalFeeRates;

import java.util.ArrayList;

public interface IFeeModel {
    FeeModelType getType();

    ArrayList<IntervalFeeRates> getRateListsByVehicleType(final String vehicleType);
}
