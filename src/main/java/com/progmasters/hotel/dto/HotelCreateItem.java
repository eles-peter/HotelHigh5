package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Hotel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HotelCreateItem {

	private String name;
	private String postalCode;
	private String city;
	private String streetAddress;
	private String hotelType;

    private String description;
	private List<String> hotelFeatures = new ArrayList<>();

    public HotelCreateItem() {
	}

	public HotelCreateItem(Hotel hotel) {
		this.name = hotel.getName();
		this.postalCode = hotel.getPostalCode();
		this.city = hotel.getCity();
		this.streetAddress = hotel.getStreetAddress();
		this.hotelType = hotel.getHotelType().name();
		this.description = hotel.getDescription();
		this.hotelFeatures = hotel.getHotelFeatures().stream().map(Enum::name).collect(Collectors.toList());
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

	public String getDescription() {
		return description;
	}

	public List<String> getHotelFeatures() {
		return hotelFeatures;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public void setHotelFeatures(List<String> hotelFeatures) {
		this.hotelFeatures = hotelFeatures;
	}



}
