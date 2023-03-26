package com.parking.components.parkingfee;

import com.parking.components.parkingfee.impl.MallBusParkingFee;
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
public class MallBusParkingFeeTest {

    @InjectMocks
    private MallBusParkingFee parkingFee;

    @Test
    @DisplayName("Test Calculate fee when location is Mall and vehicle is Bus, should return 100 fee ")
    public void calculateParkingFee() {

        List<IntervalFeeRates> rates = getIntervalFeeRates();

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 00),
                LocalDateTime.of(2023, 02, 02, 03, 59)));
        Assertions.assertEquals(fee, 100.0);
    }

    private List<IntervalFeeRates> getIntervalFeeRates() {
        return new ArrayList<>() {
            {
                add(new IntervalFeeRates(50.0));
            }
        };
    }

    @Test
    @DisplayName("Test Calculate fee when location is Mall and vehicle is Bus, and no rate list, should return 0 fee ")
    public void calculateParkingFee_2() {

        List<IntervalFeeRates> rates = null;

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 02),
                LocalDateTime.of(2023, 02, 02, 10, 02)));
        Assertions.assertEquals(fee, 0.0);
    }
}
