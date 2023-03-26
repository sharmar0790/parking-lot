package com.parking.components.parkingfee.impl;

import com.parking.components.parkingfee.IParkingFee;
import com.parking.constants.ParkingFeeType;
import com.parking.model.ParkingFeeHelper;
import com.parking.utils.Utility;
import org.springframework.stereotype.Component;

@Component
public class MallMotorcycleParkingFee implements IParkingFee {

    @Override
    public ParkingFeeType getType() {
        return ParkingFeeType.MALL_MOTORCYCLES;
    }

    @Override
    public Double calculateParkingFee(final ParkingFeeHelper feeHelper) {
        int chargedHour = 0;
        if (!Utility.isNullOrEmptyList(feeHelper.getRates())) {
            chargedHour = Utility.getChargedHours(feeHelper.getEntryDateTime(), feeHelper.getExitDateTime());
            return (double)chargedHour * feeHelper.getRates().get(0).getRate();
        } else {
            return 0.0;
        }
    }
}
