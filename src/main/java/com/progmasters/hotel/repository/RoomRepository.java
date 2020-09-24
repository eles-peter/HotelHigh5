package com.progmasters.hotel.repository;

import com.progmasters.hotel.domain.Room;
import com.progmasters.hotel.domain.RoomFeatureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r JOIN r.hotel h WHERE h.id = :hotel_id ORDER BY r.pricePerNight")
    List<Room> findAllByHotelId(@Param("hotel_id") Long hotelId);

    @Query("SELECT r FROM Room r JOIN r.hotel h JOIN r.roomFeatures f WHERE h.id = :hotel_id AND f IN :room_features GROUP BY r HAVING COUNT(r) = :count_of_matches ORDER BY r.pricePerNight")
    List<Room> findAllByHotelIdByRoomFeatures(@Param("hotel_id") Long hotelId, @Param("room_features") List<RoomFeatureType> roomFeatures, @Param("count_of_matches") Long countOfMatches);


    @Query("SELECT DISTINCT room FROM Room room JOIN room.reservations reservations WHERE reservations.endDate > :start_date AND reservations.startDate < :end_date")
    List<Room> findAllOccupiedRoomByDateRange(@Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate);

    @Query("SELECT room FROM Room room WHERE room NOT IN " +
            "(SELECT room FROM Room room JOIN room.reservations reservations WHERE reservations.endDate > :start_date AND reservations.startDate < :end_date) ")
    List<Room> findAllFreeRoomByDateRange(@Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate);



}
