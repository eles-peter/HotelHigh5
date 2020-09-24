package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Room;

public class RoomShortListItem {

    private Long id;
    private String roomName;
    private String roomType;
    private Integer numberOfBeds;
    private Double pricePerNight;

    RoomShortListItem() {
    }

    public RoomShortListItem(Room room) {
        this.id = room.getId();
        this.roomName = room.getRoomName();
        this.roomType = room.getRoomType().getDisplayName();
        this.numberOfBeds = room.getNumberOfBeds();
        this.pricePerNight = room.getPricePerNight();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
