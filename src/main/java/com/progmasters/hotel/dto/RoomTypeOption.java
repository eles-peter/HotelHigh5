package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.RoomType;

public class RoomTypeOption {


    private String name;
    private String displayName;

    public RoomTypeOption(RoomType roomType) {
        this.name = roomType.toString();
        this.displayName = roomType.getDisplayName();
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
