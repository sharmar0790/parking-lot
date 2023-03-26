package com.parking.service;

import com.parking.components.feemodel.impl.MallFeeModel;
import com.parking.components.parkingfee.impl.MallCarParkingFee;
import com.parking.constants.FeeModelType;
import com.parking.constants.ParkingFeeType;
import com.parking.factory.FeeModelFactory;
import com.parking.factory.ParkingFeeFactory;
import com.parking.model.ParkingTicket;
import com.parking.service.impl.FeeCalculationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.parking.constants.ApplicationConstants.CARS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeeCalculationServiceTest {

    @InjectMocks
    private FeeCalculationService feeCalculationService;

    @Mock
    private MallCarParkingFee mallCarParkingFee;

    @Mock
    private MallFeeModel mallFeeModel;

    @Test
    @DisplayName(" Test calculateParkFee")
    public void calculateParkFee() {
        final ParkingTicket pd = new ParkingTicket("001", CARS, "SLOT-1", "MALL", LocalDateTime.now(), null);

        Mockito.mockStatic(ParkingFeeFactory.class);
        when(ParkingFeeFactory.getParkingFee(ParkingFeeType.MALL_CARS)).thenReturn(mallCarParkingFee);

        Mockito.mockStatic(FeeModelFactory.class);
        when(FeeModelFactory.getFeeModel(FeeModelType.MALL)).thenReturn(mallFeeModel);

        Double parkingFee = feeCalculationService.calculateParkFee(pd, LocalDateTime.now());
        Assertions.assertEquals(parkingFee, 0);
    }

    @Test
    @DisplayName(" Test throw IllegalArgumentException")
    public void calculateParkFee_2() {

        final ParkingTicket pd = new ParkingTicket("001", CARS + 1, "SLOT-1", "MALL", LocalDateTime.now(), null);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> feeCalculationService.calculateParkFee(pd, LocalDateTime.now()));
    }
}
