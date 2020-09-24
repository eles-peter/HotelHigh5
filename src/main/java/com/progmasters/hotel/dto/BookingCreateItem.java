package com.progmasters.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class BookingCreateItem {

    private String guestAccountName;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String remark;
    private Integer numberOfGuests;
    private List<RoomReservationShortItem> roomReservationList;

    public BookingCreateItem() {
    }

    public String getGuestAccountName() {
        return guestAccountName;
    }

    public String getRemark() {
        return remark;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public List<RoomReservationShortItem> getRoomReservationList() {
        return roomReservationList;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setGuestAccountName(String guestAccountName) {
        this.guestAccountName = guestAccountName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setRoomReservationList(List<RoomReservationShortItem> roomReservationList) {
        this.roomReservationList = roomReservationList;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
