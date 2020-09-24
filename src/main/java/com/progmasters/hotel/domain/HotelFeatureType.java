package com.progmasters.hotel.domain;

public enum HotelFeatureType {

    PARKOLASILEHETOSEG("Ingyenes parkolás"),
    AZONNALIVISSZAIGAZOLAS("Azonnali visszaigazolás"),
    INGYENESWIFI("Ingyenes WIFI"),
    OTP_MKB_KARTYA("OTP, MKB (Szabadidő, Vendéglátás, Szálláshely)"),
    HATEVES_KORIG_INGYENES("6 éves korig ingyenes"),
    BABABARAT("Bababarát szálláshely"),
    PARKOLAS_FIZETOS("Parkolási lehetőség (Fizetős)"),
    LEGKONDICIONALAS("Légkondícionálás"),
    WELLNESS("Wellness szolgáltatások"),
    ETTEREM("Saját étterem"),
    ;

    private String displayName;

    HotelFeatureType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
