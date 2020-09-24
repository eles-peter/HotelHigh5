package com.progmasters.hotel.domain;

import com.progmasters.hotel.dto.HotelCreateItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "hotel_type")
    private HotelType hotelType;

    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms = new ArrayList<>();

    @Column(name = "hotel_image_urls")
    @ElementCollection
    private List<String> hotelImageUrls = new ArrayList<>();

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = HotelFeatureType.class)
    @CollectionTable(name = "hotel_features")
    @Column(name = "hotel_features")
    private List<HotelFeatureType> hotelFeatures = new ArrayList<>();

    @Column(name = "avg_rate")
    private Double avgRate;

    public Hotel() {
    }

    public Hotel(String name, String postalCode, String city, String streetAddress, HotelType hotelType, List<Room> rooms, List<String> hotelImageUrls, String description, List<HotelFeatureType> hotelFeatures, Double longitude, Double latitude) {
        this.name = name;
        this.postalCode = postalCode;
        this.city = city;
        this.streetAddress = streetAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.hotelType = hotelType;
        this.rooms = rooms;
        this.hotelImageUrls = hotelImageUrls;
        this.description = description;
        this.hotelFeatures = hotelFeatures;
    }

    public Hotel(HotelCreateItem hotelCreateItem) {
        this.name = hotelCreateItem.getName();
        this.postalCode = hotelCreateItem.getPostalCode();
        this.city = hotelCreateItem.getCity();
        this.streetAddress = hotelCreateItem.getStreetAddress();
        this.hotelType = HotelType.valueOf(hotelCreateItem.getHotelType());
        this.description = hotelCreateItem.getDescription();
        this.hotelFeatures = hotelCreateItem.getHotelFeatures().stream().map(HotelFeatureType::valueOf).collect(Collectors.toList());
        //TODO get longitude and latitude from some geocoding api
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public HotelType getHotelType() {
        return hotelType;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<String> getHotelImageUrls() {
        return hotelImageUrls;
    }

    public String getDescription() {
        return description;
    }

    public List<HotelFeatureType> getHotelFeatures() {
        return hotelFeatures;
    }

    public Double getAvgRate() {
        return avgRate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setHotelType(HotelType hotelType) {
        this.hotelType = hotelType;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void setHotelImageUrls(List<String> hotelImageUrl) {
        this.hotelImageUrls = hotelImageUrl;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHotelFeatures(List<HotelFeatureType> hotelFeatures) {
        this.hotelFeatures = hotelFeatures;
    }

    public void setAvgRate(Double avgRate) {
        this.avgRate = avgRate;
    }
}
