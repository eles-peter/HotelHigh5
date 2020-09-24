package com.progmasters.hotel.domain;

public enum HotelType {

	SZALLODA("szálloda"),
	HOSTEL("hostel"),
	APARTMAN("apartman"),
	PANZIO("panzió"),
	NYARALO("nyaraló"),
    ;

    private String displayName;

    private HotelType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}