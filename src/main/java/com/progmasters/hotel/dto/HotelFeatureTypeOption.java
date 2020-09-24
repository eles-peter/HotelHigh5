package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.HotelFeatureType;

public class HotelFeatureTypeOption {


    private String name;
    private String displayName;

    public HotelFeatureTypeOption(HotelFeatureType hotelFeatureType) {
        this.name = hotelFeatureType.toString();
        this.displayName = hotelFeatureType.getDisplayName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
