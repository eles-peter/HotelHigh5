package com.progmasters.hotel.domain;

import com.progmasters.hotel.dto.RoomCreateItem;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String roomName;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;

    @Min(1)
    @Max(50)
    @Column(name = "number_of_beds")
    private Integer numberOfBeds;

    @Column(name = "room_area")
    private Integer roomArea;

    @Min(1)
    @Max(1000000)
    private Double pricePerNight;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "room")
    private List<RoomReservation> reservations;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "room_image_url")
    private String roomImageUrl;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = RoomFeatureType.class)
    @CollectionTable(name = "room_room_features")
    @Column(name = "room_room_features")
    private List<RoomFeatureType> roomFeatures = new ArrayList<>();

    public Room() {
    }

    public Room(RoomCreateItem roomCreateItem) {
        this.roomName = roomCreateItem.getRoomName();
        this.roomType = RoomType.valueOf(roomCreateItem.getRoomType());
        this.numberOfBeds = roomCreateItem.getNumberOfBeds();
        this.roomArea = roomCreateItem.getRoomArea();
        this.pricePerNight = roomCreateItem.getPricePerNight();
        this.roomImageUrl = roomCreateItem.getRoomImageUrl();
        this.description = roomCreateItem.getDescription();
        this.roomFeatures = roomCreateItem.getRoomFeatures().stream().map(RoomFeatureType::valueOf).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds;
    }

    public Integer getRoomArea() {
        return roomArea;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    public List<RoomReservation> getReservations() {
        return reservations;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public String getRoomImageUrl() {
        return roomImageUrl;
    }

    public List<RoomFeatureType> getRoomFeatures() {
        return roomFeatures;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setNumberOfBeds(Integer numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public void setRoomArea(Integer roomArea) {
        this.roomArea = roomArea;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReservations(List<RoomReservation> reservations) {
        this.reservations = reservations;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void setRoomImageUrl(String roomImageUrl) {
        this.roomImageUrl = roomImageUrl;
    }

    public void setRoomFeatures(List<RoomFeatureType> roomFeatures) {
        this.roomFeatures = roomFeatures;
    }
}
