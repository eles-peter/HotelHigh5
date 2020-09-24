package com.progmasters.hotel.controller;

import com.progmasters.hotel.dto.RoomReservationData;
import com.progmasters.hotel.service.RoomReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomReservation")
public class RoomReservationController {

    private RoomReservationService roomReservationService;

    public RoomReservationController(RoomReservationService roomReservationService) {
        this.roomReservationService = roomReservationService;
    }

    @PutMapping("/{roomReservationId}")
    public ResponseEntity<Void> updateRoomReservation(@RequestBody RoomReservationData modifiedRoomReservationData, @PathVariable Long roomReservationId) {
        boolean roomReservationIsUpdated = roomReservationService.updateRoomReservation(modifiedRoomReservationData, roomReservationId);
        return roomReservationIsUpdated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping()
    public ResponseEntity<Void> updateAllRoomReservationInBooking(@RequestBody List<RoomReservationData> modifiedRoomReservations) {
        boolean roomReservationIsUpdated = roomReservationService.updateAllRoomReservationInBooking(modifiedRoomReservations);
        return roomReservationIsUpdated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") Long roomReservationId) {
        boolean isDeleteSuccessful = roomReservationService.deleteRoomReservation(roomReservationId);
        return isDeleteSuccessful ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }






}
