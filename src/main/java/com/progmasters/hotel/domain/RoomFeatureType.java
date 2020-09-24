package com.progmasters.hotel.domain;

public enum RoomFeatureType {

	TEAKAVEFOZO("Tea-/ kávéfőző"),
	HAJSZARITO("Hajszárító"),
	HUTOSZEKRENY("Hűtőszekrény"),
	TV("Síkképernyős TV"),
	BABAAGY("Babaágy betehető"),
	MINIBAR("Minibár"),
	SZOBASZEF("Szobaszéf"),
	LEGKONDICIONALT("Légkondícionálás"),
	ERKELYTERASZ("Erkély/terasz"),
	WIFI("WIFI"),
	PEZSGOFURDO("Pezsgőfürdő"),
    ;

    private String displayName;

	RoomFeatureType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}