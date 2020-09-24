package com.progmasters.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.progmasters.hotel.domain.RoomReservation;

import java.time.LocalDate;

public class RoomReservationData {

    private Long roomReservationId;

    private Long bookingId;

    private String guestFirstName;

    private String guestLastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Integer numberOfGuests;

    public RoomReservationData() {
    }

    public RoomReservationData(RoomReservation roomReservation) {
        this.roomReservationId = roomReservation.getId();
        this.bookingId = roomReservation.getBooking().getId();
        if (roomReservation.getBooking().getGuest() != null) {
            this.guestFirstName = roomReservation.getBooking().getGuest().getFirstname();
            this.guestLastName = roomReservation.getBooking().getGuest().getLastname();
        } else {
            this.guestFirstName = roomReservation.getBooking().getFirstName();
            this.guestLastName = roomReservation.getBooking().getLastName();
        }
        this.startDate = roomReservation.getStartDate();
        this.endDate = roomReservation.getEndDate();
        this.numberOfGuests = roomReservation.getBooking().getNumberOfGuests();
    }

    public Long getRoomReservationId() {
        return roomReservationId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getGuestFirstName() {
        return guestFirstName;
    }

    public String getGuestLastName() {
        return guestLastName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setRoomReservationId(Long roomReservationId) {
        this.roomReservationId = roomReservationId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public void setGuestFirstName(String guestFirstName) {
        this.guestFirstName = guestFirstName;
    }

    public void setGuestLastName(String guestLastName) {
        this.guestLastName = guestLastName;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
