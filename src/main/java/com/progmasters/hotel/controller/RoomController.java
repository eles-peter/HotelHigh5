package com.progmasters.hotel.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.progmasters.hotel.domain.RoomFeatureType;
import com.progmasters.hotel.dto.*;
import com.progmasters.hotel.service.RoomService;
import com.progmasters.hotel.validator.RoomCreateItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private RoomService roomService;
    private RoomCreateItemValidator validator;

    @Autowired
    public RoomController(RoomService roomService, RoomCreateItemValidator validator) {
        this.roomService = roomService;
        this.validator = validator;
    }

    @InitBinder("RoomCreateItem")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping("/formData")
    public ResponseEntity<RoomFormData> getRoomFormData() {
        List<RoomTypeOption> roomTypeOptionList = this.roomService.getRoomTypeOptionList();
        List<RoomFeatureTypeOption> roomFeatureTypeOptionList = this.roomService.getRoomFeatureTypeOptionList();
        RoomFormData roomFormData = new RoomFormData(roomTypeOptionList, roomFeatureTypeOptionList);
        return new ResponseEntity<>(roomFormData, HttpStatus.OK);
    }

    @GetMapping("/filter/{id}")
    public List<RoomListItem> getFilteredFreeRoomList(
            @PathVariable("id") Long hotelId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam List<String> roomFeatures) {
        if (roomFeatures.isEmpty()) {
            return roomService.getFreeRoomList(hotelId, startDate, endDate);
        } else {
            List<RoomFeatureType> roomFeatureEnumList = roomFeatures.stream().map(RoomFeatureType::valueOf).collect(Collectors.toList());
            return roomService.getFreeRoomListFilterByRoomFeature(hotelId, startDate, endDate, roomFeatureEnumList);
        }
    }

    @GetMapping("/data/{id}")
    public List<RoomBookingData> getRoomBookingDataByDateRange(
            @PathVariable("id") Long hotelId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return roomService.getRoomBookingDataByDateRange(hotelId, startDate, endDate);
    }

    @GetMapping("/{id}")
    public RoomDetails roomDetail(@PathVariable("id") Long id) {
        return roomService.getRoomDetails(id);
    }

    @PostMapping
    public ResponseEntity createRoom(@RequestBody @Valid RoomCreateItem roomCreateItem) {
        boolean roomIsCreated = roomService.createRoomInHotel(roomCreateItem);
        return roomIsCreated ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<RoomListItem>> deleteRoom(@PathVariable Long id) {
        Long hotelId = roomService.getHotelIdByRoomId(id);
        boolean isDeleteSuccessful = roomService.deleteRoom(id);
        ResponseEntity<List<RoomListItem>> result;
        if (isDeleteSuccessful) {
            result = new ResponseEntity<>(roomService.getRoomList(hotelId), HttpStatus.OK);
        } else {
            result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @GetMapping("/formData/{id}")
    public ResponseEntity<RoomCreateItem> getRoomForUpdate(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(roomService.getRoomCreateItem(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomCreateItem> updateRoom(@Valid @RequestBody RoomCreateItem roomCreateItem, @PathVariable Long id) {
        boolean roomIsUpdated = roomService.updateRoom(roomCreateItem, id);
        return roomIsUpdated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
