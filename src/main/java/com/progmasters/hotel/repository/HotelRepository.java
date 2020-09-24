package com.progmasters.hotel.repository;

import com.progmasters.hotel.domain.Hotel;
import com.progmasters.hotel.domain.HotelFeatureType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("SELECT h.id FROM Hotel h")
    List<Long> findAllHotelId();

    @Query("SELECT h FROM Hotel h WHERE h.name = :name")
    Optional<Object> findByHotelName(String name);

    @Query("SELECT h AS filteredHotel, MIN(room.pricePerNight/room.numberOfBeds) AS bestPrice " +
            "FROM Hotel h JOIN h.rooms room " +
            "WHERE h.id = :id " +
            "GROUP BY h.id")
    HotelFilterResult findByIdWithBestPrice(Long id);

    @Query("SELECT h AS filteredHotel, MIN(room.pricePerNight/room.numberOfBeds) AS bestPrice " +
            "FROM Hotel h JOIN h.rooms room " +
            "GROUP BY h.id " +
            "ORDER BY MIN(room.pricePerNight/room.numberOfBeds)")
    Page<HotelFilterResult> findAllOrderByBestPrice(Pageable pageable);

    @Query("SELECT h AS filteredHotel, MIN(room.pricePerNight/room.numberOfBeds) AS bestPrice " +
            "FROM Hotel h JOIN h.rooms room " +
            "GROUP BY h.id " +
            "ORDER BY h.avgRate DESC")
    Page<HotelFilterResult> findAllOrderByBestAvgRateForHomePage(Pageable pageable);

    @Query("SELECT h AS filteredHotel, MIN(room.pricePerNight/room.numberOfBeds) AS bestPrice " +
            "FROM Hotel h JOIN h.rooms room WHERE room NOT IN " +
            "(SELECT room FROM Room room JOIN room.reservations reservations " +
            "WHERE reservations.endDate > :start_date AND reservations.startDate < :end_date) " +
            "GROUP BY h.id HAVING SUM(room.numberOfBeds) >= :number_of_guests " +
            "ORDER BY MIN(room.pricePerNight/room.numberOfBeds)")
    Page<HotelFilterResult> findAllByDateAndPersonFilterOrderByBestPrice
            (@Param("start_date") LocalDate startDate,
             @Param("end_date") LocalDate endDate,
             @Param("number_of_guests") long numberOfGuests,
             Pageable pageable);

    @Query("SELECT h AS filteredHotel, MIN(room.pricePerNight/room.numberOfBeds) AS bestPrice " +
            "FROM Hotel h JOIN h.rooms room " +
            "WHERE room NOT IN " +
            "(SELECT room FROM Room room JOIN room.reservations reservations " +
            "WHERE reservations.endDate > :start_date AND reservations.startDate < :end_date) " +
            "AND h IN " +
            "(SELECT h FROM Hotel h JOIN h.hotelFeatures f " +
            "WHERE f IN :hotel_features GROUP BY h HAVING COUNT(h) = :count_of_matches) " +
            "GROUP BY h.id HAVING SUM(room.numberOfBeds) >= :number_of_guests " +
            "ORDER BY MIN(room.pricePerNight/room.numberOfBeds)")
    Page<HotelFilterResult> findAllByDatePersonAndFeaturesFilterOrderByBestPrice
            (@Param("start_date") LocalDate startDate,
             @Param("end_date") LocalDate endDate,
             @Param("number_of_guests") long numberOfGuests,
             @Param("hotel_features") List<HotelFeatureType> hotelFeatures,
             @Param("count_of_matches") Long countOfMatches,
             Pageable pageable);

    interface HotelFilterResult {
        Hotel getFilteredHotel();
        Double getBestPrice();
    }

}
