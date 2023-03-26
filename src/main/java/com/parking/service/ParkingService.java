package com.parking.service;

import com.parking.model.ParkingTicket;
import com.parking.model.UnParkingDetails;
import com.parking.repository.ParkingRepository;
import com.parking.service.impl.FeeCalculationService;
import com.parking.utils.DateConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import static com.parking.constants.ApplicationConstants.DATE_TIME_FORMATTER;
import static com.parking.constants.ApplicationConstants.R_STR;
import static com.parking.constants.ApplicationConstants.SLOT_STR;

@Service
public class ParkingService {
    private static final Logger LOG = LoggerFactory.getLogger(ParkingService.class);
    private final String parkingLocation;
    private final ParkingRepository parkingRepository;
    private final FeeCalculationService feeCalculationService;
    private final ReentrantLock lock = new ReentrantLock();

    public ParkingService(final ParkingRepository parkingRepository,
                          final FeeCalculationService feeCalculationService,
                          @Value("${parking.location}") String parkingLocation) {
        this.parkingRepository = parkingRepository;
        this.feeCalculationService = feeCalculationService;
        this.parkingLocation = parkingLocation;
    }

    public Optional<ParkingTicket> parkVehicle(final String vehicleType) {
        try {
            lock.lock();
            LOG.info("Parking a vehicle. . {}", vehicleType);
            int slot = this.parkingRepository.getAndReduceAvailableSlotsByVehicleType(vehicleType);
            int ticketNumber = this.parkingRepository.getNextValMySequence();
            String stringTicketNumber = String.format("%03d", ticketNumber);
            LOG.info("stringTicketNumber. . {}", stringTicketNumber);
            final ParkingTicket parkingTicket = buildParkingDetails(vehicleType, slot, stringTicketNumber);
            this.parkingRepository.save(parkingTicket);
            return Optional.of(parkingTicket);
        } finally {
            lock.unlock();
        }
    }

    private ParkingTicket buildParkingDetails(final String vehicleType, int slot, final String ticketNumber) {
        final String localDateTime = DATE_TIME_FORMATTER.format(LocalDateTime.now());
        return new ParkingTicket(ticketNumber, vehicleType, SLOT_STR + slot, this.parkingLocation,
                DateConverterUtils.convertStringToDate(localDateTime), null);
    }

    public Optional<UnParkingDetails> unParkVehicle(final String ticketNumber) {
        try {
            lock.lock();
            Optional<ParkingTicket> parkVehicleDetail = this.parkingRepository.findParkingByTicketNumber(ticketNumber);
            Optional<UnParkingDetails> unParkingDetails = Optional.empty();
            if (parkVehicleDetail.isPresent()) {
                LocalDateTime endTime = LocalDateTime.now();
                Double fee = this.feeCalculationService.calculateParkFee(parkVehicleDetail.get(), endTime);
                unParkingDetails = buildUnParkingDetailsObject(parkVehicleDetail.get(), endTime, fee);
                parkVehicleDetail.get().setExitDateTime(endTime);
                this.parkingRepository.save(parkVehicleDetail.get());
                this.parkingRepository.updateAvailableSlotsByVehicleType(parkVehicleDetail.get().getVehicleType());
            }
            return unParkingDetails;
        } finally {
            lock.unlock();
        }
    }

    private Optional<UnParkingDetails> buildUnParkingDetailsObject(final ParkingTicket parkVehicleDetail,
                                                                   final LocalDateTime endTime, final Double fee) {
        String endTimeStr = DATE_TIME_FORMATTER.format(endTime);
        return Optional.of(new UnParkingDetails(R_STR + parkVehicleDetail.getTicketNumber(),
                fee,
                parkVehicleDetail.getEntryDateTime(),
                DateConverterUtils.convertStringToDate(endTimeStr)));
    }
}
