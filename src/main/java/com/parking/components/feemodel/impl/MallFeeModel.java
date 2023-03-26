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
public class MallFeeModel implements IFeeModel {

    private static final Map<String, ArrayList<IntervalFeeRates>> FLAT_RATE_MAP = new HashMap<>() {
        {
            put(MOTORCYCLES, new ArrayList<>() {
                {
                    add(new IntervalFeeRates(10.0));
                }
            });
            put(CARS, new ArrayList<>() {
                {
                    add(new IntervalFeeRates(20.0));
                }
            });
            put(BUSES, new ArrayList<>() {
                {
                    add(new IntervalFeeRates(50.0));
                }
            });
        }
    };
    private static final Map<String, ArrayList<IntervalFeeRates>> PER_HOUR_FLAT_RATE = Collections.unmodifiableMap(
            FLAT_RATE_MAP);

    @Override
    public FeeModelType getType() {
        return FeeModelType.MALL;
    }

    @Override
    public ArrayList<IntervalFeeRates> getRateListsByVehicleType(String vehicleType) {
        return PER_HOUR_FLAT_RATE.get(vehicleType);
    }
}
