package com.parking.service.impl;

import com.parking.components.feemodel.IFeeModel;
import com.parking.components.parkingfee.IParkingFee;
import com.parking.constants.FeeModelType;
import com.parking.constants.ParkingFeeType;
import com.parking.factory.FeeModelFactory;
import com.parking.factory.ParkingFeeFactory;
import com.parking.model.IntervalFeeRates;
import com.parking.model.ParkingFeeHelper;
import com.parking.model.ParkingTicket;
import com.parking.service.IFeeCalculationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.parking.constants.ApplicationConstants.UNDERSCORE_STR;

@Service
public class FeeCalculationService implements IFeeCalculationService {

    private static final Logger LOG = LoggerFactory.getLogger(FeeCalculationService.class);

    @Override
    public Double calculateParkFee(final ParkingTicket parkVehicleDetail, final LocalDateTime parkingEndTime) {

        try {
            final String location = parkVehicleDetail.getLocation();
            final String vehicleType = parkVehicleDetail.getVehicleType();

            LOG.info("Calculating the parking fee by vehicle type. . {}, location. . {} and duration. . {}",
                    vehicleType,
                    location, parkingEndTime);

            final IFeeModel feeModelObj = FeeModelFactory.getFeeModel(
                    FeeModelType.valueOf(parkVehicleDetail.getLocation()));
            final List<IntervalFeeRates> rateByVehicleType = feeModelObj.getRateListsByVehicleType(
                    parkVehicleDetail.getVehicleType());
            final String type = location + UNDERSCORE_STR + vehicleType;
            final IParkingFee parkingFeeObject = ParkingFeeFactory.getParkingFee(ParkingFeeType.valueOf(type));
            if (ObjectUtils.isEmpty(parkingFeeObject)) return 0.0;
            final ParkingFeeHelper parkingFeeHelper = new ParkingFeeHelper(rateByVehicleType,
                    parkVehicleDetail.getEntryDateTime(), parkingEndTime);
            return parkingFeeObject.calculateParkingFee(parkingFeeHelper);
        } catch (Exception ex) {
            LOG.error("Caught error while fee calculation");
            throw new IllegalArgumentException("Caught error while fee calculation", ex);
        }
    }
}
