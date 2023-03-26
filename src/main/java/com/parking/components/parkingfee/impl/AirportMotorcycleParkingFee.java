package com.parking.components.parkingfee.impl;

import com.parking.components.parkingfee.IParkingFee;
import com.parking.constants.ParkingFeeType;
import com.parking.model.IntervalFeeRates;
import com.parking.model.ParkingFeeHelper;
import com.parking.utils.Utility;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static com.parking.constants.ApplicationConstants.NUMBER_3600;
import static com.parking.constants.ApplicationConstants.NUMBER_60;

@Component
public class AirportMotorcycleParkingFee implements IParkingFee {

    @Override
    public ParkingFeeType getType() {
        return ParkingFeeType.AIRPORT_MOTORCYCLES;
    }

    @Override
    public Double calculateParkingFee(final ParkingFeeHelper feeHelper) {
        if (Utility.isNullOrEmptyList(feeHelper.getRates())) return 0.0;

        double fee = 0;
        int chargedSeconds =
                Utility.getChargedMinutes(feeHelper.getEntryDateTime(), feeHelper.getExitDateTime()) * NUMBER_60;

        for (IntervalFeeRates rate : feeHelper.getRates()) {
            int startTimeRangeInSeconds = rate.getStartHour() * NUMBER_3600;
            int endTimeRangeInSeconds = rate.getEndHour() != null ? rate.getEndHour() * NUMBER_3600 : 0;
            if (ObjectUtils.isEmpty(rate.getEndHour()) && chargedSeconds >= startTimeRangeInSeconds) {
                int chargedDays = Utility.getChargedDays(feeHelper.getEntryDateTime(), feeHelper.getExitDateTime());
                fee = chargedDays * rate.getRate();
            } else {
                if (chargedSeconds >= startTimeRangeInSeconds &&
                        chargedSeconds < endTimeRangeInSeconds) {
                    fee = rate.getRate();
                }
            }
        }
        return fee;
    }
}
