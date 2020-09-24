package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Hotel;

public class HotelShortItem {

    private Long id;
    private String name;
    private String postalCode;
    private String city;
    private String streetAddress;
    private String hotelType;

    HotelShortItem() {
    }

    public HotelShortItem(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.postalCode = hotel.getPostalCode();
        this.city = hotel.getCity();
        this.streetAddress = hotel.getStreetAddress();
        this.hotelType = hotel.getHotelType().getDisplayName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

}
