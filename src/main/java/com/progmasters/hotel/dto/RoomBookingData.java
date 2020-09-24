package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Room;

import java.util.List;

public class RoomBookingData {

    private Long roomId;
    private String roomName;
    private String roomType;
    private Integer numberOfBeds;
    private Double pricePerNight;
    private List<RoomReservationData> roomReservationDataList;

    RoomBookingData() {
    }

    public RoomBookingData(Room room) {
        this.roomId = room.getId();
        this.roomName = room.getRoomName();
        this.roomType = room.getRoomType().getDisplayName();
        this.numberOfBeds = room.getNumberOfBeds();
        this.pricePerNight = room.getPricePerNight();
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(Integer numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public List<RoomReservationData> getRoomReservationDataList() {
        return roomReservationDataList;
    }

    public void setRoomReservationDataList(List<RoomReservationData> roomReservationDataList) {
        this.roomReservationDataList = roomReservationDataList;
    }
}
