package com.progmasters.hotel.repository;

import com.progmasters.hotel.domain.Room;
import com.progmasters.hotel.domain.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {

    List<RoomReservation> findAllByRoomAndEndDateAfterAndStartDateBefore(Room room, LocalDate startDate, LocalDate endDate);

    List<RoomReservation> findAllByRoomIdAndEndDateAfterAndStartDateBefore(Long roomId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT DISTINCT r.room  FROM RoomReservation r WHERE r.endDate > :start_date AND r.startDate < :end_date")
    List<Room> findAllOccupiedRoomIdByDateRange(@Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate);


    @Query("SELECT reservation FROM RoomReservation reservation " +
            "WHERE reservation.room = :room AND reservation.endDate > :start_date AND reservation.startDate < :end_date " +
            "ORDER BY reservation.startDate")
    List<RoomReservation> findAllByRoomIdAndDateRange(@Param("room") Room room, @Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate);


}
