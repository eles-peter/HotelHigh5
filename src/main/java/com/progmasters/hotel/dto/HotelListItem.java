package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Hotel;

public class HotelListItem {

    private static final int SHORTDESCRIPTIONLENGTH = 200;

    private Long id;
    private String name;
    private String postalCode;
    private String city;
    private String streetAddress;
    private Double longitude;
    private Double latitude;
    private String hotelType;
    private String hotelImageUrl;
    private String shortDescription;
    private Integer bestPricePerNightPerPerson;
    private Double avgRate;

    HotelListItem() {
    }

    public HotelListItem(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.postalCode = hotel.getPostalCode();
        this.city = hotel.getCity();
        this.streetAddress = hotel.getStreetAddress();
        this.longitude = hotel.getLongitude();
        this.latitude = hotel.getLatitude();
        this.hotelType = hotel.getHotelType().getDisplayName();
        if (!hotel.getHotelImageUrls().isEmpty()) {
            this.hotelImageUrl = hotel.getHotelImageUrls().get(0);
        }
        if (hotel.getDescription() != null && hotel.getDescription().length() > SHORTDESCRIPTIONLENGTH) {
            String hotelFullDescription = hotel.getDescription();
            String endOfTheDescription = (hotelFullDescription.substring(SHORTDESCRIPTIONLENGTH).split(" "))[0] + "...";
            this.shortDescription = hotelFullDescription.substring(0, SHORTDESCRIPTIONLENGTH) + endOfTheDescription;
        } else {
            this.shortDescription = hotel.getDescription();
        }
        this.avgRate = hotel.getAvgRate();
    }

    public HotelListItem(Hotel hotel, int bestPricePerNightPerPerson) {
        this(hotel);
        this.setBestPricePerNightPerPerson(bestPricePerNightPerPerson);
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

    public String getHotelType() {
        return hotelType;
    }

    public String getHotelImageUrl() {
        return hotelImageUrl;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Integer getBestPricePerNightPerPerson() {
        return bestPricePerNightPerPerson;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setBestPricePerNightPerPerson(Integer bestPricePerNightPerPerson) {
        this.bestPricePerNightPerPerson = bestPricePerNightPerPerson;
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

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public void setHotelImageUrl(String hotelImageUrl) {
        this.hotelImageUrl = hotelImageUrl;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Double getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(Double avgRate) {
        this.avgRate = avgRate;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}

