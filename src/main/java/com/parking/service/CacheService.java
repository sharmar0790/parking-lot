package com.parking.service;

import com.parking.model.ParkingSpots;
import com.parking.repository.ParkingSpotsRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {

    private final static Logger LOG = LoggerFactory.getLogger(CacheService.class);
    private static final Map<String, Integer> NUMBER_OF_SPOTS_MAP = new ConcurrentHashMap<>();
    private final ParkingSpotsRepository parkingSpotsRepository;

    public CacheService(final ParkingSpotsRepository parkingSpotsRepository) {
        this.parkingSpotsRepository = parkingSpotsRepository;
    }

    @PostConstruct
    public void postConstruct() {
        LOG.info("fetching parking-spots from DB to save it in cache");
        List<ParkingSpots> parkingSpots = this.parkingSpotsRepository.findAll();
        parkingSpots.forEach(spot -> NUMBER_OF_SPOTS_MAP.put(spot.getVehicleType(), spot.getSpotNumber()));
    }

    public Optional<Map<String, Integer>> getAvailableSlots() {
        return Optional.of(NUMBER_OF_SPOTS_MAP);
    }

    public Optional<Integer> getAvailableSlotsByVehicle(String vehicleType_upperCase) {
        return Optional.ofNullable(NUMBER_OF_SPOTS_MAP.get(vehicleType_upperCase));
    }

    public void reduceAvailableSlotsByVehicle(String vehicleType) {
        NUMBER_OF_SPOTS_MAP.put(vehicleType, NUMBER_OF_SPOTS_MAP.get(vehicleType) - 1);
    }

    public void updateAvailableSlotsByVehicleType(final String vehicleType) {
        NUMBER_OF_SPOTS_MAP.put(vehicleType, NUMBER_OF_SPOTS_MAP.get(vehicleType) + 1);
    }
}
