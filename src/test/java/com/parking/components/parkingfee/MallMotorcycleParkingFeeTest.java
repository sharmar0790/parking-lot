package com.parking.components.parkingfee;

import com.parking.components.parkingfee.impl.MallMotorcycleParkingFee;
import com.parking.model.IntervalFeeRates;
import com.parking.model.ParkingFeeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MallMotorcycleParkingFeeTest {

    @InjectMocks
    private MallMotorcycleParkingFee parkingFee;

    @Test
    @DisplayName("Test Calculate fee when location is Mall and vehicle is MotorCycle, should return 40 fee ")
    public void calculateParkingFee() {

        List<IntervalFeeRates> rates = getIntervalFeeRates();

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 02),
                LocalDateTime.of(2023, 02, 02, 05, 32)));
        Assertions.assertEquals(fee, 40.0);
    }

    private List<IntervalFeeRates> getIntervalFeeRates() {
        return new ArrayList<>() {
            {
                add(new IntervalFeeRates(10.0));
            }
        };
    }

    @Test
    @DisplayName("Test Calculate fee when location is Mall and vehicle is MotorCycle, and no rate list, should return 0 fee ")
    public void calculateParkingFee_2() {

        List<IntervalFeeRates> rates = null;

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 02),
                LocalDateTime.of(2023, 02, 02, 10, 02)));
        Assertions.assertEquals(fee, 0.0);
    }
}
