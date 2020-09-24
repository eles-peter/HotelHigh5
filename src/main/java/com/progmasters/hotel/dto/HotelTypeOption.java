package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.HotelType;

public class HotelTypeOption {

    private String name;
    private String displayName;

    public HotelTypeOption(HotelType hotelType) {
        this.name = hotelType.toString();
        this.displayName = hotelType.getDisplayName();
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
