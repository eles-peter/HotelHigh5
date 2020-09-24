package com.progmasters.hotel.repository;

import com.progmasters.hotel.domain.Booking;
import com.progmasters.hotel.domain.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b JOIN b.roomReservations reservations WHERE reservations.room.id = :room_id ")
    List<Booking> findAllByRoomId(@Param("room_id") Long roomId);

    @Query("SELECT b,reservations.startDate FROM Booking b JOIN b.roomReservations reservations WHERE reservations.room.id = :room_id " +
            "AND reservations.startDate <= :current_date AND reservations.endDate >= :current_date " +
            "ORDER BY reservations.startDate")
    List<Booking> findCurrentByRoomId(@Param("room_id") Long roomId, @Param("current_date") LocalDate date);

    @Query("SELECT b FROM Booking b JOIN b.roomReservations reservations WHERE reservations.room.id = :room_id " +
            "AND reservations.startDate > :current_date " +
            "ORDER BY reservations.startDate")
    Page<Booking> findFutureByRoomId(@Param("room_id") Long roomId, @Param("current_date") LocalDate date, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.roomReservations reservations WHERE reservations.room.id = :room_id " +
            "AND reservations.endDate < :current_date " +
            "ORDER BY reservations.startDate DESC")
    Page<Booking> findPastByRoomId(@Param("room_id") Long roomId, @Param("current_date") LocalDate date, Pageable pageable);

    @Query("SELECT DISTINCT b FROM Booking b JOIN b.roomReservations reservations JOIN reservations.room room JOIN room.hotel hotel WHERE hotel.id = :hotel_id ")
    List<Booking> findAllByHotelId(@Param("hotel_id") Long hotelId);

    @Query("SELECT DISTINCT b AS resultBooking ,reservations.startDate AS resultStartDate FROM Booking b JOIN b.roomReservations reservations JOIN reservations.room room JOIN room.hotel hotel WHERE hotel.id = :hotel_id " +
            "AND reservations.startDate <= :current_date AND reservations.endDate >= :current_date")
    Page<BookingResult> findCurrentByHotelId(@Param("hotel_id") Long hotelId, @Param("current_date") LocalDate date, Pageable pageable);

    @Query("SELECT DISTINCT b AS resultBooking ,reservations.startDate AS resultStartDate FROM Booking b JOIN b.roomReservations reservations JOIN reservations.room room JOIN room.hotel hotel WHERE hotel.id = :hotel_id " +
            "AND reservations.startDate > :current_date")
    Page<BookingResult> findFutureByHotelId(@Param("hotel_id") Long hotelId, @Param("current_date") LocalDate date, Pageable pageable);

    @Query("SELECT DISTINCT b AS resultBooking ,reservations.startDate AS resultStartDate FROM Booking b JOIN b.roomReservations reservations JOIN reservations.room room JOIN room.hotel hotel WHERE hotel.id = :hotel_id " +
            "AND reservations.endDate < :current_date")
    Page<BookingResult> findPastByHotelId(@Param("hotel_id") Long hotelId, @Param("current_date") LocalDate date, Pageable pageable);

    @Query("SELECT DISTINCT b FROM Booking b WHERE b.guest.id = :user_id")
    List<Booking> findAllByUserId(@Param("user_id") Long userId);

    @Query("SELECT DISTINCT b,reservations.startDate FROM Booking b JOIN b.roomReservations reservations WHERE b.guest.id = :user_id " +
            "AND reservations.startDate <= :current_date AND reservations.endDate >= :current_date " +
            "ORDER BY reservations.startDate")
    List<Booking> findCurrentByUserId(@Param("user_id") Long userId, @Param("current_date")LocalDate date);

    @Query("SELECT DISTINCT b,reservations.startDate  FROM Booking b JOIN b.roomReservations reservations WHERE b.guest.id = :user_id " +
            "AND reservations.startDate > :current_date " +
            "ORDER BY reservations.startDate")
    List<Booking> findFutureByUserId(@Param("user_id") Long userId,  @Param("current_date")LocalDate date);

    @Query("SELECT DISTINCT b,reservations.startDate FROM Booking b JOIN b.roomReservations reservations WHERE b.guest.id = :user_id " +
            "AND reservations.endDate < :current_date " +
            "ORDER BY reservations.startDate DESC")
    List<Booking> findPastByUserId(@Param("user_id") Long userId,  @Param("current_date")LocalDate date);

    interface BookingResult {
        Booking getResultBooking();
        LocalDate getResultStartDate();
    }

}
