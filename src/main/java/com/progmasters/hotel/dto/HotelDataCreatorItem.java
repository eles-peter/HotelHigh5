package com.progmasters.hotel.dto;

import java.util.ArrayList;
import java.util.List;

public class HotelDataCreatorItem {

    String name;
    String postalCode;
    String city;
    String streetAddress;
    Double longitude;
    Double latitude;
    String hotelType;
    List<String> hotelImageUrls = new ArrayList<>();
    List<String> hotelFeatures = new ArrayList<>();
    String description;

    List<RoomCreateItem> roomList = new ArrayList<>();

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

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getHotelType() {
        return hotelType;
    }

    public List<String> getHotelImageUrls() {
        return hotelImageUrls;
    }

    public List<String> getHotelFeatures() {
        return hotelFeatures;
    }

    public List<RoomCreateItem> getRoomList() {
        return roomList;
    }

    public String getDescription() {
        return description;
    }
}
