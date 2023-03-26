package com.parking.repository;

import com.parking.model.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingTicket, Integer>, ParkingRepositoryCustom {

    @Query(value = "select * FROM Parking_Ticket t where t.ticket_Number= ?1", nativeQuery = true)
    Optional<ParkingTicket> findParkingByTicketNumber(final String ticketNumber);

    @Query(value = "select nextval('parking_ticket_sequence')", nativeQuery = true)
    Integer getNextValMySequence();
}
