package com.progmasters.hotel.dto;

import java.util.List;

public class RoomFormData {

	private List<RoomTypeOption> roomTypeList;
	private List<RoomFeatureTypeOption> roomFeaturesList;

	public RoomFormData(List<RoomTypeOption> roomTypeList, List<RoomFeatureTypeOption> roomFeaturesList) {
		this.roomTypeList = roomTypeList;
		this.roomFeaturesList = roomFeaturesList;
	}

	public List<RoomTypeOption> getRoomType() {
		return roomTypeList;
	}

	public List<RoomFeatureTypeOption> getRoomFeatures() {
		return roomFeaturesList;
	}

}
