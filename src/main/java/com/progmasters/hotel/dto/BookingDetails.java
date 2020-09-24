package com.progmasters.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.progmasters.hotel.domain.Booking;
import com.progmasters.hotel.domain.RoomReservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDetails {

    private Long id;
    private HotelShortItem hotel;
    private AccountDetails guest;
    private String remark;
    private List<RoomReservationDetails> roomReservationList = new ArrayList<>();
    private Integer numberOfGuests;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm")
    private LocalDateTime dateOfBooking;
    private Double priceOfBooking;


    public BookingDetails() {
    }

    public BookingDetails(Booking booking) {
        this.id = booking.getId();
        this.hotel = new HotelShortItem(booking.getRoomReservations().get(0).getRoom().getHotel());
        if (booking.getGuest() != null) {
            this.guest = new AccountDetails(booking.getGuest());
        } else {
            this.guest = new AccountDetails();
            this.guest.setFirstname(booking.getFirstName());
            this.guest.setLastname(booking.getLastName());
            this.guest.setAddress(booking.getAddress());
            this.guest.setUsername(booking.getEmail());
        }
        this.remark = booking.getRemark();
        for (RoomReservation roomReservation : booking.getRoomReservations()) {
            this.roomReservationList.add(new RoomReservationDetails(roomReservation));
        }
        this.numberOfGuests = booking.getNumberOfGuests();
        this.dateOfBooking = booking.getDateOfBooking();
        this.priceOfBooking = booking.getPriceOfBooking();
    }

    public Long getId() {
        return id;
    }

    public HotelShortItem getHotel() {
        return hotel;
    }

    public AccountDetails getGuest() {
        return guest;
    }

    public String getRemark() {
        return remark;
    }

    public List<RoomReservationDetails> getRoomReservationList() {
        return roomReservationList;
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

    public void setHotel(HotelShortItem hotel) {
        this.hotel = hotel;
    }

    public void setGuest(AccountDetails guest) {
        this.guest = guest;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setRoomReservationList(List<RoomReservationDetails> roomReservationList) {
        this.roomReservationList = roomReservationList;
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
