package com.progmasters.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.progmasters.hotel.domain.Booking;
import com.progmasters.hotel.domain.RoomReservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingListItemForHotel {

    private Long id;
    private AccountListItem guest;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private List<RoomShortListItem> reservedRooms = new ArrayList<>();
    private Integer numberOfGuests;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm")
    private LocalDateTime dateOfBooking;
    private Double priceOfBooking;


    BookingListItemForHotel() {
    }

    public BookingListItemForHotel(Booking booking) {
        this.id = booking.getId();
        if (booking.getGuest() != null) {
            this.guest = new AccountListItem(booking.getGuest());
        } else {
            this.guest = new AccountListItem();
            this.guest.setFirstname(booking.getFirstName());
            this.guest.setLastname(booking.getLastName());
            this.guest.setAddress(booking.getAddress());
            this.guest.setUsername(booking.getEmail());
        }
        this.startDate = booking.getRoomReservations().get(0).getStartDate();
        this.endDate = booking.getRoomReservations().get(0).getEndDate();
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

    public AccountListItem getGuest() {
        return guest;
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

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public LocalDateTime getDateOfBooking() {
        return dateOfBooking;
    }

    public Double getPriceOfBooking() {
        return priceOfBooking;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGuest(AccountListItem guest) {
        this.guest = guest;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<RoomShortListItem> getReservedRooms() {
        return reservedRooms;
    }

    public void setReservedRooms(List<RoomShortListItem> reservedRooms) {
        this.reservedRooms = reservedRooms;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setDateOfBooking(LocalDateTime dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public void setPriceOfBooking(Double priceOfBooking) {
        this.priceOfBooking = priceOfBooking;
    }
}
