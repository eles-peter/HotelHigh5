package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Room;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomCreateItem {

    @NotNull(message = "Room name must not be empty")
    @Size(min = 1, max = 200, message = "Room name must be between 1 and 200 characters")
    private String roomName;

    @NotNull(message = "Number of beds must be between 1 and 50")
    @Min(value = 1, message = "Number of beds must be between 1 and 50")
    @Max(value = 50, message = "Number of beds must be between 1 and 50")
    private Integer numberOfBeds;

    @NotNull(message = "Price must be between 1 and 1000000")
    @Min(value = 1, message = "Price must be between 1 and 1000000")
    @Max(value = 1000000, message = "Price must be between 1 and 1000000")
    private Double pricePerNight;

    private String roomType;
    private Integer roomArea;
    private String description;
    private String roomImageUrl;
    private List<String> roomFeatures = new ArrayList<>();
    private Long hotelId;

    public RoomCreateItem(Room room) {
        this.roomName = room.getRoomName();
        this.roomType = room.getRoomType().name();
        this.numberOfBeds = room.getNumberOfBeds();
        this.roomArea = room.getRoomArea();
        this.pricePerNight = room.getPricePerNight();
        this.roomImageUrl = room.getRoomImageUrl();
        this.description = room.getDescription();
        this.roomFeatures = room.getRoomFeatures().stream().map(Enum::name).collect(Collectors.toList());
        this.hotelId = room.getHotel().getId();
    }

    RoomCreateItem() {
    }

    public String getRoomName() {
        return roomName;
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public Integer getRoomArea() {
        return roomArea;
    }

    public String getDescription() {
        return description;
    }

    public String getRoomImageUrl() {
        return roomImageUrl;
    }

    public List<String> getRoomFeatures() {
        return roomFeatures;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setNumberOfBeds(Integer numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setRoomArea(Integer roomArea) {
        this.roomArea = roomArea;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoomImageUrl(String roomImageUrl) {
        this.roomImageUrl = roomImageUrl;
    }

    public void setRoomFeatures(List<String> roomFeatures) {
        this.roomFeatures = roomFeatures;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
