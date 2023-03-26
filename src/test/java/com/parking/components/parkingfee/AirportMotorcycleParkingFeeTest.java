package com.parking.components.parkingfee;

import com.parking.components.parkingfee.impl.AirportMotorcycleParkingFee;
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
public class AirportMotorcycleParkingFeeTest {

    @InjectMocks
    private AirportMotorcycleParkingFee parkingFee;

    @Test
    @DisplayName("Test Calculate fee when location is Airport and vehicle is MotorCycle, should return 0 fee ")
    public void calculateParkingFee() {

        List<IntervalFeeRates> rates = getIntervalFeeRates();

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 02),
                LocalDateTime.of(2023, 02, 02, 04, 02)));
        Assertions.assertEquals(fee, 40.0);
    }

    private List<IntervalFeeRates> getIntervalFeeRates() {
        List<IntervalFeeRates> rates = new ArrayList<>() {
            {
                add(new IntervalFeeRates(0, 1, 0.0));
                add(new IntervalFeeRates(1, 8, 40.0));
                add(new IntervalFeeRates(8, 24, 60.0));
                add(new IntervalFeeRates(24, null, 80.0));
            }
        };
        return rates;
    }

    @Test
    @DisplayName("Test Calculate fee when location is Airport and vehicle is MotorCycle, should return 60 fee ")
    public void calculateParkingFee_2() {

        List<IntervalFeeRates> rates = getIntervalFeeRates();

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 02),
                LocalDateTime.of(2023, 02, 02, 10, 02)));
        Assertions.assertEquals(fee, 60.0);
    }

    @Test
    @DisplayName("Test Calculate fee when location is Airport and vehicle is MotorCycle, should return 160 fee ")
    public void calculateParkingFee_3() {

        List<IntervalFeeRates> rates = getIntervalFeeRates();

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 00),
                LocalDateTime.of(2023, 02, 03, 14, 0)));
        Assertions.assertEquals(fee, 160);
    }
}
