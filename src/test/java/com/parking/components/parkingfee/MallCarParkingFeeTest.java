package com.parking.components.parkingfee;

import com.parking.components.parkingfee.impl.MallCarParkingFee;
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
public class MallCarParkingFeeTest {

    @InjectMocks
    private MallCarParkingFee parkingFee;

    @Test
    @DisplayName("Test Calculate fee when location is Mall and vehicle is Car, should return 140 fee ")
    public void calculateParkingFee() {

        List<IntervalFeeRates> rates = getIntervalFeeRates();

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 02),
                LocalDateTime.of(2023, 02, 02, 8, 03)));
        Assertions.assertEquals(fee, 140);
    }

    private List<IntervalFeeRates> getIntervalFeeRates() {
        return new ArrayList<>() {
            {
                add(new IntervalFeeRates(20.0));
            }
        };
    }

    @Test
    @DisplayName("Test Calculate fee when location is Mall and vehicle is Car, and no rate list, should return 0 fee ")
    public void calculateParkingFee_2() {

        List<IntervalFeeRates> rates = null;

        Double fee = parkingFee.calculateParkingFee(new ParkingFeeHelper(rates,
                LocalDateTime.of(2023, 02, 02, 02, 02),
                LocalDateTime.of(2023, 02, 02, 10, 02)));
        Assertions.assertEquals(fee, 0);
    }
}
