package com.parking.components.feemodel.impl;

import com.parking.components.feemodel.IFeeModel;
import com.parking.constants.FeeModelType;
import com.parking.model.IntervalFeeRates;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.parking.constants.ApplicationConstants.BUSES;
import static com.parking.constants.ApplicationConstants.CARS;
import static com.parking.constants.ApplicationConstants.MOTORCYCLES;

@Component
public class StadiumFeeModel implements IFeeModel {

    private static final Map<String, ArrayList<IntervalFeeRates>> FLAT_RATE_MAP = new HashMap<>() {
        {
            put(MOTORCYCLES, new ArrayList<>() {
                {
                    add(new IntervalFeeRates(0, 4, 30.0));
                    add(new IntervalFeeRates(4, 12, 60.0));
                    add(new IntervalFeeRates(12, null, 100.0));
                }
            });
            put(CARS, new ArrayList<>() {
                {
                    add(new IntervalFeeRates(0, 4, 60.0));
                    add(new IntervalFeeRates(4, 12, 120.0));
                    add(new IntervalFeeRates(12, null, 200.0));
                }
            });
            put(BUSES, null);
        }
    };
    private static final Map<String, ArrayList<IntervalFeeRates>> PER_HOUR_RATE = Collections.unmodifiableMap(
            FLAT_RATE_MAP);

    @Override
    public FeeModelType getType() {
        return FeeModelType.STADIUM;
    }

    @Override
    public ArrayList<IntervalFeeRates> getRateListsByVehicleType(final String vehicleType) {
        return PER_HOUR_RATE.get(vehicleType);
    }
}