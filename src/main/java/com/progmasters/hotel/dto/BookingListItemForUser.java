package com.progmasters.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.progmasters.hotel.domain.Booking;
import com.progmasters.hotel.domain.RoomReservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingListItemForUser {

    private Long id;
    private String guestName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private HotelShortItem hotel;
    private List<RoomShortListItem> reservedRooms = new ArrayList<>();
    private Integer numberOfGuests;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm")
    private LocalDateTime dateOfBooking;
    private Double priceOfBooking;


    BookingListItemForUser() {
    }

    public BookingListItemForUser(Booking booking) {
        this.id = booking.getId();
        this.guestName = booking.getGuest().getFirstname() + " " + booking.getGuest().getLastname();
        this.startDate = booking.getRoomReservations().get(0).getStartDate();
        this.endDate = booking.getRoomReservations().get(0).getEndDate();
        this.hotel = new HotelShortItem(booking.getRoomReservations().get(0).getRoom().getHotel());
        for (RoomReservation roomReservation : booking.getRoomReservations()) {
            this.reservedRooms.add(new RoomShortListItem(roomReservation.getRoom()));
        }
        this.numberOfGuests = booking.getNumberOfGuests();
        this.dateOfBooking = booking.getDateOfBooking();
        this.priceOfBooking = booking.getPriceOfBooking();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public HotelShortItem getHotel() {
        return hotel;
    }

    public void setHotel(HotelShortItem hotel) {
        this.hotel = hotel;
    }

    public List<RoomShortListItem> getReservedRooms() {
        return reservedRooms;
    }

    public void setReservedRooms(List<RoomShortListItem> reservedRooms) {
        this.reservedRooms = reservedRooms;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public LocalDateTime getDateOfBooking() {
        return dateOfBooking;
    }

    public void setDateOfBooking(LocalDateTime dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public Double getPriceOfBooking() {
        return priceOfBooking;
    }

    public void setPriceOfBooking(Double priceOfBooking) {
        this.priceOfBooking = priceOfBooking;
    }
}
