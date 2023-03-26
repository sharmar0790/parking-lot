package com.parking.components.feemodel.impl;

import com.parking.components.feemodel.IFeeModel;
import com.parking.constants.FeeModelType;
import com.parking.model.IntervalFeeRates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.parking.constants.ApplicationConstants.BUSES;
import static com.parking.constants.ApplicationConstants.CARS;
import static com.parking.constants.ApplicationConstants.MOTORCYCLES;

@Component
public class AirportFeeModel implements IFeeModel {
    private static final Logger LOG = LoggerFactory.getLogger(AirportFeeModel.class);
    private static final Map<String, ArrayList<IntervalFeeRates>> FLAT_RATE_MAP = new HashMap<>() {
        {
            put(MOTORCYCLES, new ArrayList<>() {
                {
                    add(new IntervalFeeRates(0, 1, 0.0));
                    add(new IntervalFeeRates(1, 8, 40.0));
                    add(new IntervalFeeRates(8, 24, 60.0));
                    add(new IntervalFeeRates(24, null, 80.0));
                }
            });
            put(CARS, new ArrayList<>() {
                {
                    add(new IntervalFeeRates(0, 12, 60.0));
                    add(new IntervalFeeRates(12, 24, 80.0));
                    add(new IntervalFeeRates(12, null, 100.0));
                }
            });
            put(BUSES, null);
        }
    };
    private static final Map<String, ArrayList<IntervalFeeRates>> PER_HOUR_RATE = Collections.unmodifiableMap(
            FLAT_RATE_MAP);

    @Override
    public FeeModelType getType() {
        return FeeModelType.AIRPORT;
    }

    @Override
    public ArrayList<IntervalFeeRates> getRateListsByVehicleType(final String vehicleType) {
        return PER_HOUR_RATE.get(vehicleType);
    }
}
