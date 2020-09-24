package com.progmasters.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.progmasters.hotel.domain.RoomReservation;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class RoomReservationDetails {

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private int numberOfNights;
    private RoomShortListItem room;

    RoomReservationDetails() {
    }

    public RoomReservationDetails(RoomReservation roomReservation) {
        this.id = roomReservation.getId();
        this.startDate = roomReservation.getStartDate();
        this.endDate = roomReservation.getEndDate();
        this.numberOfNights = (int)DAYS.between(roomReservation.getStartDate(), roomReservation.getEndDate());
        this.room = new RoomShortListItem(roomReservation.getRoom());
    }

    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public RoomShortListItem getRoom() {
        return room;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public void setRoom(RoomShortListItem room) {
        this.room = room;
    }
}
