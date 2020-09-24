package com.progmasters.hotel.validator;

import com.progmasters.hotel.dto.RoomCreateItem;
import com.progmasters.hotel.service.RoomService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RoomCreateItemValidator implements Validator {

    private final RoomService validatorService;

    public RoomCreateItemValidator(RoomService validatorService) {
        this.validatorService = validatorService;
    }

    @Override
    public boolean supports(Class clazz) {
        return RoomCreateItem.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        RoomCreateItem roomCreateItem = (RoomCreateItem) object;

        String roomName = roomCreateItem.getRoomName();
        if (roomName == null || roomName.isBlank()) {
            errors.rejectValue("roomName", "room.roomName.isempty");
        }

        int numberOfBeds = roomCreateItem.getNumberOfBeds();
        if (numberOfBeds < 1) {
            errors.rejectValue("numberOfBeds", "room.numberOfBeds.lessthanone");
        }

        int roomArea = roomCreateItem.getRoomArea();
        if (roomArea < 0) {
            errors.rejectValue("roomArea", "room.roomArea.lessthanzero");
        }

        double pricePerNight = roomCreateItem.getPricePerNight();
        if (pricePerNight < 0) {
            errors.rejectValue("pricePerNight", "room.pricePerNight.lessthanzero");
        }

        String roomType = roomCreateItem.getRoomType();
        if (roomType == null || roomType.isBlank()) {
            errors.rejectValue("roomType", "room.roomType.isempty");
        }

        String description = roomCreateItem.getDescription();
        if (description.length() < 0 || description.length() > 200) {
            errors.rejectValue("description", "room.description.istoolongorshort");
        }

    }

}
