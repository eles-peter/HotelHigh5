package com.progmasters.hotel.validator;

import com.progmasters.hotel.dto.HotelCreateItem;
import com.progmasters.hotel.service.HotelService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class HotelCreateItemValidator implements Validator {

	private final HotelService validatorService;

	public HotelCreateItemValidator(HotelService validatorService) {
        this.validatorService = validatorService;
    }

	@Override
    public boolean supports(Class clazz) {
        return HotelCreateItem.class.equals(clazz);
    }

	@Override
    public void validate(Object object, Errors errors) {
        HotelCreateItem hotelCreateItem = (HotelCreateItem) object;

        String hotelName = hotelCreateItem.getName();
        if (hotelName == null || hotelName.isBlank()) {
            errors.rejectValue("name", "hotel.name.isempty");
        }

        String postalCode = hotelCreateItem.getPostalCode();
        if (postalCode == null || postalCode.isBlank()) {
            errors.rejectValue("postalCode", "hotel.postalCode.isempty");
        }

        String city = hotelCreateItem.getCity();
        if (city == null || city.isBlank()) {
            errors.rejectValue("city", "hotel.city.isempty");
        }

        String streetAddress = hotelCreateItem.getStreetAddress();
        if (streetAddress == null || streetAddress.isBlank()) {
            errors.rejectValue("streetAddress", "hotel.streetAddress.isempty");
        }

        String hotelType = hotelCreateItem.getHotelType();
        if (hotelType == null || hotelType.isBlank()) {
            errors.rejectValue("hotelType", "hotel.hotelType.isempty");
        }

        String description = hotelCreateItem.getDescription();
        if (description.length() < 500 || description.length() > 2000) {
            errors.rejectValue("description", "hotel.description.istoolongorshort");
        }

	}

}
