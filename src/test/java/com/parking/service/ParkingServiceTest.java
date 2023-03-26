package com.parking.service;

import com.parking.model.ParkingTicket;
import com.parking.model.UnParkingDetails;
import com.parking.repository.ParkingRepository;
import com.parking.service.impl.FeeCalculationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.parking.constants.ApplicationConstants.CARS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    @Mock
    private ParkingRepository parkingRepository;

    @Mock
    private FeeCalculationService feeCalculationService;

    @InjectMocks
    private ParkingService parkingService;

    @Test
    @DisplayName(" Test park vehicle success")
    public void parkVehicle() {
        final String vehicleType = CARS;
        final ParkingTicket pd = buildParkingDetailsTest(vehicleType);
        doReturn(1).when(parkingRepository).getAndReduceAvailableSlotsByVehicleType(vehicleType);
        Optional<ParkingTicket> parkingTicket = parkingService.parkVehicle(vehicleType);
        Assertions.assertEquals(parkingTicket.get().getVehicleType(), vehicleType);
        Assertions.assertEquals(parkingTicket.get().getSpotNumber(), pd.getSpotNumber());
    }

    private ParkingTicket buildParkingDetailsTest(String vehicleType) {
        return new ParkingTicket("001", vehicleType, "SLOT-1", "MALL", LocalDateTime.now(), null);
    }

    @Test
    @DisplayName(" Test unParkVehicle")
    public void unParkVehicle() {
        final ParkingTicket pd = buildParkingDetailsTest(CARS);
        doReturn(Optional.of(pd)).when(parkingRepository).findParkingByTicketNumber("001");
        doNothing().when(parkingRepository).updateAvailableSlotsByVehicleType(pd.getVehicleType());
        doReturn(20.0).when(feeCalculationService).calculateParkFee(any(ParkingTicket.class), any(LocalDateTime.class));
        UnParkingDetails unParkingDetails = parkingService.unParkVehicle("001").get();
        Assertions.assertEquals(unParkingDetails.getReceiptNumber(), "R-" + pd.getTicketNumber());
        Assertions.assertEquals(unParkingDetails.getFee(), 20);
    }

    @Test
    @DisplayName(" Test unParkVehicle with 0 fee")
    public void unParkVehicle_2() {
        buildParkingDetailsTest(CARS);
        doReturn(Optional.empty()).when(parkingRepository).findParkingByTicketNumber("001");
        Optional<UnParkingDetails> unParkingDetails = parkingService.unParkVehicle("001");
        Assertions.assertTrue(unParkingDetails.isEmpty());
    }
}
