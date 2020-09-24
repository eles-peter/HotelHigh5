package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Room;
import com.progmasters.hotel.domain.RoomFeatureType;

import java.util.ArrayList;
import java.util.List;

public class RoomListItem {

    private Long id;
    private String roomName;
    private String roomType;
    private Integer numberOfBeds;
    private Integer roomArea;
    private Double pricePerNight;
    private String roomImageUrl;
    private String description;
    private List<String> roomFeatures = new ArrayList<>();

    RoomListItem() {
    }

    public RoomListItem(Room room) {
        this.id = room.getId();
        this.roomName = room.getRoomName();
        this.roomType = room.getRoomType().getDisplayName();
        this.numberOfBeds = room.getNumberOfBeds();
        this.roomArea = room.getRoomArea();
        this.pricePerNight = room.getPricePerNight();
        this.roomImageUrl = room.getRoomImageUrl();
        this.description = room.getDescription();
        for (RoomFeatureType roomFeaturesType : room.getRoomFeatures()) {
            this.roomFeatures.add(roomFeaturesType.getDisplayName());
        }
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

    public Integer getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(Integer roomArea) {
        this.roomArea = roomArea;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getRoomImageUrl() {
        return roomImageUrl;
    }

    public void setRoomImageUrl(String roomImageUrl) {
        this.roomImageUrl = roomImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRoomFeatures() {
        return roomFeatures;
    }

    public void setRoomFeatures(List<String> roomFeatures) {
        this.roomFeatures = roomFeatures;
    }
}
