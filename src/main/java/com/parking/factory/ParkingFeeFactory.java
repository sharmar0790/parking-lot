package com.parking.factory;

import com.parking.components.parkingfee.IParkingFee;
import com.parking.constants.ParkingFeeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public final class ParkingFeeFactory {

    private static Map<ParkingFeeType, IParkingFee> parkingFeeMap;

    @Autowired
    private ParkingFeeFactory(final List<IParkingFee> parkingFees) {
        parkingFeeMap = parkingFees.stream().collect(
                Collectors.toUnmodifiableMap(IParkingFee::getType, Function.identity()));
    }

    public static IParkingFee getParkingFee(final ParkingFeeType parkingFeeType) {
        return Optional.ofNullable(parkingFeeMap.get(parkingFeeType)).orElseThrow(IllegalArgumentException::new);
    }

}
