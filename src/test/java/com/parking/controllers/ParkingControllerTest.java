package com.parking.controllers;

import com.parking.model.ParkingTicket;
import com.parking.model.UnParkingDetails;
import com.parking.service.CacheService;
import com.parking.service.ParkingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.parking.constants.ApplicationConstants.AVAILABLE_SLOTS_EP;
import static com.parking.constants.ApplicationConstants.BUSES;
import static com.parking.constants.ApplicationConstants.CARS;
import static com.parking.constants.ApplicationConstants.MOTORCYCLES;
import static com.parking.constants.ApplicationConstants.PARKING_EP;
import static com.parking.constants.ApplicationConstants.PARK_EP;
import static com.parking.constants.ApplicationConstants.SLASH;
import static com.parking.constants.ApplicationConstants.UN_PARK_EP;
import static com.parking.constants.ApplicationConstants.V1;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ParkingController.class)
public class ParkingControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ParkingService parkingService;

    @MockBean
    private CacheService cacheService;

    @Test
    @DisplayName("Test Car Parked un-successfully, no slots found")
    public void parkVehicleFailed() throws Exception {
        final String vehicleType = CARS;
        mvc.perform(MockMvcRequestBuilders
                        .post(V1 + PARKING_EP + PARK_EP + SLASH + vehicleType)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test Car Parked successfully")
    public void parkVehicle() throws Exception {
        final ParkingTicket pd = new ParkingTicket("001", CARS, "SLOT-1", "MALL", LocalDateTime.now(), null);

        doReturn(Optional.of(pd)).when(parkingService).parkVehicle(CARS);
        doReturn(Optional.of(2)).when(cacheService).getAvailableSlotsByVehicle(CARS);

        mvc.perform(MockMvcRequestBuilders
                        .post(V1 + PARKING_EP + PARK_EP + SLASH + CARS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.spotNumber").value(pd.getSpotNumber()))
                .andExpect(jsonPath("$.ticketNumber").value(pd.getTicketNumber()));
    }

    @Test
    @DisplayName("Test Car Parked failed when wrong vehicle type passed")
    public void parkVehicleFailed_2() throws Exception {
        final String vehicleType = "CARST";

        mvc.perform(MockMvcRequestBuilders
                        .post(V1 + PARKING_EP + PARK_EP + SLASH + vehicleType)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test Return INTERNAL_SERVER_ERROR parking a vehicle")
    public void parkVehicleFailed_3() throws Exception {
        doReturn(Optional.of(2)).when(cacheService).getAvailableSlotsByVehicle(CARS);
        doThrow(new RuntimeException()).when(parkingService).parkVehicle(CARS);
        mvc.perform(MockMvcRequestBuilders
                        .post(V1 + PARKING_EP + PARK_EP + SLASH + CARS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Test Car Un-Parked failed with wrong ticket number passed")
    public void unParkVehicle() throws Exception {
        final String ticketNumber = "001";
        doReturn(Optional.empty()).when(parkingService).unParkVehicle(ticketNumber);
        mvc.perform(MockMvcRequestBuilders
                        .post(V1 + PARKING_EP + UN_PARK_EP + SLASH + ticketNumber)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test Car Un-Parked success")
    public void unParkVehicle_2() throws Exception {
        final String ticketNumber = "001";
        final UnParkingDetails upd = new UnParkingDetails("R-" + 1, 10.0, LocalDateTime.now(), LocalDateTime.now());
        doReturn(Optional.of(upd)).when(parkingService).unParkVehicle(ticketNumber);
        mvc.perform(MockMvcRequestBuilders
                        .post(V1 + PARKING_EP + UN_PARK_EP + SLASH + ticketNumber)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.fee").value(upd.getFee()))
                .andExpect(jsonPath("$.receiptNumber").value(upd.getReceiptNumber()));
    }

    @Test
    @DisplayName("Test Return INTERNAL_SERVER_ERROR un parking a vehicle")
    public void unParkVehicle_3() throws Exception {
        doThrow(new RuntimeException()).when(parkingService).unParkVehicle("001");
        mvc.perform(MockMvcRequestBuilders
                        .post(V1 + PARKING_EP + UN_PARK_EP + SLASH + "001")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Test Display Available slots")
    public void getAvailableSlots() throws Exception {
        final Map<String, Integer> availSlots = new HashMap<>() {
            {
                put(CARS, 5);
                put(BUSES, 5);
                put(MOTORCYCLES, 5);
            }
        };
        doReturn(Optional.of(availSlots)).when(cacheService).getAvailableSlots();
        mvc.perform(MockMvcRequestBuilders
                        .get(V1 + PARKING_EP + SLASH + AVAILABLE_SLOTS_EP)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.CARS").value(5));
    }

    @Test
    @DisplayName("Test Return INTERNAL_SERVER_ERROR by Available slots")
    public void getAvailableSlots_2() throws Exception {
        final String vehicleType = CARS;
        doThrow(new RuntimeException()).when(cacheService).getAvailableSlots();
        mvc.perform(MockMvcRequestBuilders
                        .get(V1 + PARKING_EP + SLASH + AVAILABLE_SLOTS_EP)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Test Display Available slots by VehicleType")
    public void getAvailableSlotsByVehicleType() throws Exception {
        final String vehicleType = CARS;
        Optional<Map<String, Integer>> availableSlots = cacheService.getAvailableSlots();
        if(availableSlots.isEmpty()){
            availableSlots = Optional.of(new HashMap<>());
        }
        availableSlots.get().put(vehicleType, 5);
        when(cacheService.getAvailableSlotsByVehicle(vehicleType)).thenReturn(Optional.of(5));
        mvc.perform(MockMvcRequestBuilders
                        .get(V1 + PARKING_EP + SLASH + AVAILABLE_SLOTS_EP + SLASH + vehicleType)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));
    }

    @Test
    @DisplayName("Test Return BAD_REQUEST by Available slots when VehicleType is not valid")
    public void getAvailableSlotsByVehicleType_2() throws Exception {
        final String vehicleType = "EV";
        doThrow(new RuntimeException()).when(cacheService).getAvailableSlotsByVehicle(vehicleType);
        mvc.perform(MockMvcRequestBuilders
                        .get(V1 + PARKING_EP + SLASH + AVAILABLE_SLOTS_EP + SLASH + vehicleType)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test Return INTERNAL_SERVER_ERROR by Available slots when there is some exception")
    public void getAvailableSlotsByVehicleType_3() throws Exception {
        final String vehicleType = CARS;
        doThrow(new RuntimeException()).when(cacheService).getAvailableSlotsByVehicle(vehicleType);
        mvc.perform(MockMvcRequestBuilders
                        .get(V1 + PARKING_EP + SLASH + AVAILABLE_SLOTS_EP + SLASH + vehicleType)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isInternalServerError());
    }
}
