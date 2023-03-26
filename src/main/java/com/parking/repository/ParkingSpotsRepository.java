package com.parking.repository;

import com.parking.model.ParkingSpots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpotsRepository extends JpaRepository<ParkingSpots, Integer> {
}
