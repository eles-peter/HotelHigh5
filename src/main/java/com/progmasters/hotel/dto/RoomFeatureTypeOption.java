package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.RoomFeatureType;

public class RoomFeatureTypeOption {


    private String name;
    private String displayName;

    public RoomFeatureTypeOption(RoomFeatureType roomFeatureType) {
        this.name = roomFeatureType.toString();
        this.displayName = roomFeatureType.getDisplayName();
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
