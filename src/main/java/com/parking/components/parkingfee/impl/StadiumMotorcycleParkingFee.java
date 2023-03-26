package com.parking.components.parkingfee.impl;

import com.parking.components.parkingfee.IParkingFee;
import com.parking.constants.ParkingFeeType;
import com.parking.model.IntervalFeeRates;
import com.parking.model.ParkingFeeHelper;
import com.parking.utils.Utility;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class StadiumMotorcycleParkingFee implements IParkingFee {

    @Override
    public ParkingFeeType getType() {
        return ParkingFeeType.STADIUM_MOTORCYCLES;
    }

    @Override
    public Double calculateParkingFee(final ParkingFeeHelper feeHelper) {

        if (Utility.isNullOrEmptyList(feeHelper.getRates())) return 0.0;

        double fee = 0;
        int chargedHour = Utility.getChargedHours(feeHelper.getEntryDateTime(), feeHelper.getExitDateTime());

        for (IntervalFeeRates rate : feeHelper.getRates()) {
            int durationOfHour = 0;
            if (chargedHour <= 0) {
                break;
            }
            if (ObjectUtils.isEmpty(rate.getEndHour())) {
                fee += chargedHour * rate.getRate();
            } else {
                if (chargedHour >= rate.getStartHour() || chargedHour < rate.getEndHour()) {
                    durationOfHour = rate.getEndHour() - rate.getStartHour();
                    fee += rate.getRate();
                    chargedHour -= durationOfHour;
                }
            }
        }
        return fee;
    }
}
