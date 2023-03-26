package com.parking.controllers;

import com.parking.model.ParkingTicket;
import com.parking.model.UnParkingDetails;
import com.parking.service.CacheService;
import com.parking.service.ParkingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

import static com.parking.constants.ApplicationConstants.ACCEPTABLE_VEHICLE_TYPE;
import static com.parking.constants.ApplicationConstants.AVAILABLE_SLOTS_EP;
import static com.parking.constants.ApplicationConstants.PARKING_EP;
import static com.parking.constants.ApplicationConstants.PARK_EP;
import static com.parking.constants.ApplicationConstants.SLASH;
import static com.parking.constants.ApplicationConstants.UN_PARK_EP;
import static com.parking.constants.ApplicationConstants.V1;

@RestController
@RequestMapping(path = V1 + PARKING_EP,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ParkingController {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingController.class);

    private final ParkingService parkingService;
    private final CacheService cacheService;

    public ParkingController(final ParkingService parkingService,
                             final CacheService cacheService) {
        this.parkingService = parkingService;
        this.cacheService = cacheService;
    }

    @PostMapping(PARK_EP + "/{vehicleType}")
    public ResponseEntity<ParkingTicket> parkVehicle(@PathVariable final String vehicleType) {
        LOG.info("Parking . . .{}", vehicleType);
        final String vehicleType_upperCase = vehicleType.toUpperCase();
        if (validateVehicleType(vehicleType_upperCase)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Optional<Integer> availableSlotsByVehicle = this.cacheService.getAvailableSlotsByVehicle(
                    vehicleType_upperCase);
            if (availableSlotsByVehicle.isPresent() && availableSlotsByVehicle.get() > 0) {
                final Optional<ParkingTicket> parkingTicket = this.parkingService.parkVehicle(vehicleType_upperCase);
                return ResponseEntity.accepted().body(parkingTicket.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error(
                    "Error caught while parking a vehicle with type. . {}. . ex. . {}", vehicleType, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean validateVehicleType(final String vehicleType) {
        return !ACCEPTABLE_VEHICLE_TYPE.contains(vehicleType);
    }

    @PostMapping(UN_PARK_EP + "/{ticketNumber}")
    public ResponseEntity<UnParkingDetails> unParkVehicle(@PathVariable final String ticketNumber) {
        LOG.info("Un-Parking . . .{}", ticketNumber);
        try {
            final Optional<UnParkingDetails> unParkingDetails = this.parkingService.unParkVehicle(ticketNumber);
            if (unParkingDetails.isPresent()) {
                return ResponseEntity.accepted().body(unParkingDetails.get());
            } else {
                LOG.error(" No vehicle details found with ticket number . . .{}", ticketNumber);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error(
                    "Error caught . . {}. . while un-parking a vehicle with ticket number. . {}", e, ticketNumber);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(AVAILABLE_SLOTS_EP)
    public ResponseEntity<Map<String, Integer>> getAvailableSlots() {
        LOG.info("Finding all the available slots");
        try {
            Optional<Map<String, Integer>> availableSlots = this.cacheService.getAvailableSlots();
            return ResponseEntity.ok(availableSlots.get());
        } catch (Exception e) {
            LOG.error(
                    "Error caught while finding the available slots ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(AVAILABLE_SLOTS_EP + SLASH + "{vehicleType}")
    public ResponseEntity<Integer> getAvailableSlotsByVehicle(@PathVariable final String vehicleType) {
        LOG.info("Finding all the available slots for vehicle type. . {}", vehicleType);
        final String vehicleType_upperCase = vehicleType.toUpperCase();
        if (validateVehicleType(vehicleType_upperCase)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Optional<Integer> availableSlots = this.cacheService.getAvailableSlotsByVehicle(vehicleType_upperCase);
            return ResponseEntity.ok(availableSlots.get());
        } catch (Exception ex) {
            LOG.error(
                    "Error caught while finding the available slots for the vehicle type . . {} and exception is . . {}",
                    vehicleType, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
