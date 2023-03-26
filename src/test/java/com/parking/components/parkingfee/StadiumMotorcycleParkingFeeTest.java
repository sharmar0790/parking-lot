package com.parking.components.parkingfee;

import com.parking.components.parkingfee.impl.StadiumMotorcycleParkingFee;
import com.parking.model.IntervalFeeRates;
import com.parking.model.ParkingFeeHelper;
import io.cucumber.java.eo.Do;
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
public class StadiumMotorcycleParkingFeeTest {

    @InjectMocks
    private StadiumMotorcycleParkingFee parkingFee;

    @Test
    @DisplayName("Test Calculate fee when location is Stadium and vehicle is MotorCycle, should return 30 fee ")
    public void calculateParkingFee() {

        List<IntervalFeeRates> rates = getIntervalFeeRates();

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 02),
                LocalDateTime.of(2023, 02, 02, 04, 02)));
        Assertions.assertEquals(fee, 30.0);
    }

    @Test
    @DisplayName("Test Calculate fee when location is Stadium and vehicle is MotorCycle, should return 90 fee ")
    public void calculateParkingFee_2() {

        List<IntervalFeeRates> rates = getIntervalFeeRates();

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 02),
                LocalDateTime.of(2023, 02, 02, 10, 02)));
        Assertions.assertEquals(fee, 90.0);
    }

    @Test
    @DisplayName("Test Calculate fee when location is Stadium and vehicle is MotorCycle, should return 390 fee ")
    public void calculateParkingFee_3() {

        List<IntervalFeeRates> rates = getIntervalFeeRates();

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 00),
                LocalDateTime.of(2023, 02, 02, 16, 59)));
        Assertions.assertEquals(fee, 390.0);
    }

    private List<IntervalFeeRates> getIntervalFeeRates() {
        List<IntervalFeeRates> rates = new ArrayList<>() {
            {
                add(new IntervalFeeRates(0, 4, 30.0));
                add(new IntervalFeeRates(4, 12, 60.0));
                add(new IntervalFeeRates(12, null, 100.0));
            }
        };
        return rates;
    }
}
