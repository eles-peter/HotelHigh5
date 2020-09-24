package com.progmasters.hotel.dto;


import com.progmasters.hotel.domain.Room;
import com.progmasters.hotel.domain.RoomFeatureType;

import java.util.ArrayList;
import java.util.List;

public class RoomDetails {

    private Long id;
    private String roomName;
    private String roomType;
    private Integer numberOfBeds;
    private Integer roomArea;
    private Long hotelId;
    private String hotelName;
    private Double pricePerNight;
    private String roomImageUrl;
    private String description;
    private List<String> roomFeatures = new ArrayList<>();

    public RoomDetails() {
    }

    public RoomDetails(Room room) {
        this.id = room.getId();
        this.roomName = room.getRoomName();
        this.roomType = room.getRoomType().getDisplayName();
        this.numberOfBeds = room.getNumberOfBeds();
        this.roomArea = room.getRoomArea();
        this.hotelId = room.getHotel().getId();
        this.hotelName = room.getHotel().getName();
        this.pricePerNight = room.getPricePerNight();
        this.roomImageUrl = room.getRoomImageUrl();
        this.description = room.getDescription();
        for (RoomFeatureType roomFeatureType : room.getRoomFeatures()) {
            this.roomFeatures.add(roomFeatureType.getDisplayName());
        }
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds;
    }

    public Integer getRoomArea() {
        return roomArea;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public String getRoomImageUrl() {
        return roomImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getRoomFeatures() {
        return roomFeatures;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setNumberOfBeds(Integer numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public void setRoomArea(Integer roomArea) {
        this.roomArea = roomArea;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setRoomImageUrl(String roomImageUrl) {
        this.roomImageUrl = roomImageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoomFeatures(List<String> roomFeatures) {
        this.roomFeatures = roomFeatures;
    }
}



