package com.progmasters.hotel.dto;

import java.util.List;

public class HotelFormData {

	private List<HotelTypeOption> hotelTypeList;
	private List<HotelFeatureTypeOption> hotelFeaturesList;

	public HotelFormData(List<HotelTypeOption> hotelTypeList, List<HotelFeatureTypeOption> hotelFeaturesList) {
		this.hotelTypeList = hotelTypeList;
		this.hotelFeaturesList = hotelFeaturesList;
	}

	public List<HotelTypeOption> getHotelType() {
		return hotelTypeList;
	}

	public List<HotelFeatureTypeOption> getHotelFeatures() {
		return hotelFeaturesList;
	}


}
