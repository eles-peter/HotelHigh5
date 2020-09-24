package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Hotel;
import com.progmasters.hotel.domain.HotelFeatureType;
import com.progmasters.hotel.domain.Room;

import java.util.ArrayList;
import java.util.List;

public class HotelDetailItem {

    private Long id;
	private String name;
	private String postalCode;
	private String city;
	private String streetAddress;
	private String hotelType;
	private Integer hotelCapacity;
	private List<RoomListItem> rooms = new ArrayList<>();
	private List<String> hotelImageUrl;
	private String description;
	private List<String> hotelFeatures = new ArrayList<>();
	private Double avgRate;

	HotelDetailItem() {
	}

	public HotelDetailItem(Hotel hotel) {
		this.id = hotel.getId();
		this.name = hotel.getName();
		this.postalCode = hotel.getPostalCode();
		this.city = hotel.getCity();
		this.streetAddress = hotel.getStreetAddress();
		this.hotelType = hotel.getHotelType().getDisplayName();
		this.hotelCapacity = 0;
		this.hotelImageUrl = hotel.getHotelImageUrls();
		this.hotelCapacity = hotel.getRooms().stream().mapToInt(Room::getNumberOfBeds).sum();
		this.description = hotel.getDescription();
		for (HotelFeatureType hotelFeatureType : hotel.getHotelFeatures()) {
			this.hotelFeatures.add(hotelFeatureType.getDisplayName());
		}
		this.avgRate = hotel.getAvgRate();
	}

    public Long getId() {
        return id;
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

	public Integer getHotelCapacity() {
		return hotelCapacity;
	}

	public List<RoomListItem> getRooms() {
		return rooms;
	}

	public List<String> getHotelImageUrl() {
		return hotelImageUrl;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getHotelFeatures() {
		return hotelFeatures;
	}

	public Double getAvgRate() {
		return avgRate;
	}

	public void setAvgRate(Double avgRate) {
		this.avgRate = avgRate;
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

	public void setHotelCapacity(Integer hotelCapacity) {
		this.hotelCapacity = hotelCapacity;
	}

	public void setRooms(List<RoomListItem> rooms) {
		this.rooms = rooms;
	}

	public void setHotelImageUrl(List<String> hotelImageUrl) {
		this.hotelImageUrl = hotelImageUrl;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setHotelFeatures(List<String> hotelFeatures) {
		this.hotelFeatures = hotelFeatures;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
