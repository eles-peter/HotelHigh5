package com.progmasters.hotel.controller;

import com.progmasters.hotel.dto.*;
import com.progmasters.hotel.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private static final int NUM_OF_ELEMENTS_PER_PAGE = 10;

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Long> saveBooking(@RequestBody BookingCreateItem bookingCreateItem) {
        Long bookingId = bookingService.saveBooking(bookingCreateItem);
        return bookingId == null ?
                new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT) :
                new ResponseEntity<>(bookingId, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") Long bookingId) {
        boolean isDeleteSuccessful = bookingService.deleteBooking(bookingId);
        return isDeleteSuccessful ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDetails> bookingDetail(@PathVariable("id") Long id) {
        BookingDetails bookingDetails = bookingService.getBookingDetails(id);
        return bookingDetails == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(bookingDetails, HttpStatus.OK);
    }

    @GetMapping("room/{id}")
    public ResponseEntity<List<BookingListItemForHotel>> getBookingListByRoom(@PathVariable("id") Long roomId) {
        return new ResponseEntity<>(bookingService.getBookingListByRoom(roomId), HttpStatus.OK);
    }

    @GetMapping("room/current/{id}")
    public ResponseEntity<List<BookingListItemForHotel>> getCurrentBookingListByRoom(@PathVariable("id") Long roomId) {
        return new ResponseEntity<>(bookingService.getCurrentBookingListByRoom(roomId), HttpStatus.OK);
    }

    @GetMapping("room/future/{id}")
    public ResponseEntity<BookingSubListForHotel> getFutureBookingListByRoom
            (@PathVariable("id") Long roomId, @RequestParam(required = false) Integer listPageNumber) {
        if (listPageNumber == null) listPageNumber = 0;
        return new ResponseEntity<>(bookingService.getFutureBookingListByRoom(roomId, listPageNumber, NUM_OF_ELEMENTS_PER_PAGE), HttpStatus.OK);
    }

    @GetMapping("room/past/{id}")
    public ResponseEntity<BookingSubListForHotel> getPastBookingListByRoom
            (@PathVariable("id") Long roomId, @RequestParam(required = false) Integer listPageNumber) {
        if (listPageNumber == null) listPageNumber = 0;
        return new ResponseEntity<>(bookingService.getPastBookingListByRoom(roomId, listPageNumber, NUM_OF_ELEMENTS_PER_PAGE), HttpStatus.OK);
    }

    @GetMapping("hotel/{id}")
    public ResponseEntity<List<BookingListItemForHotel>> getBookingListByHotel(@PathVariable("id") Long hotelId) {
        return new ResponseEntity<>(bookingService.getBookingListByHotel(hotelId), HttpStatus.OK);
    }

    @GetMapping("hotel/current/{id}")
    public ResponseEntity<BookingSubListForHotel> getCurrentBookingListByHotel
            (@PathVariable("id") Long hotelId, @RequestParam(required = false) Integer listPageNumber) {
        if (listPageNumber == null) listPageNumber = 0;
        return new ResponseEntity<>(bookingService.getCurrentBookingListByHotel(hotelId, listPageNumber, NUM_OF_ELEMENTS_PER_PAGE), HttpStatus.OK);
    }

    @GetMapping("hotel/future/{id}")
    public ResponseEntity<BookingSubListForHotel> getFutureBookingListByHotel
            (@PathVariable("id") Long hotelId, @RequestParam(required = false) Integer listPageNumber) {
        if (listPageNumber == null) listPageNumber = 0;
        return new ResponseEntity<>(bookingService.getFutureBookingListByHotel(hotelId, listPageNumber, NUM_OF_ELEMENTS_PER_PAGE), HttpStatus.OK);
    }

    @GetMapping("hotel/past/{id}")
    public ResponseEntity<BookingSubListForHotel> getPastBookingListByHotel
            (@PathVariable("id") Long hotelId, @RequestParam(required = false) Integer listPageNumber) {
        if (listPageNumber == null) listPageNumber = 0;
        return new ResponseEntity<>(bookingService.getPastBookingListByHotel(hotelId, listPageNumber, NUM_OF_ELEMENTS_PER_PAGE), HttpStatus.OK);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<BookingListItemForUser>> getBookingListByUser(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(bookingService.getBookingListByUser(userId), HttpStatus.OK);
    }

    @GetMapping("user/current/{id}")
    public ResponseEntity<List<BookingListItemForUser>> getCurrentBookingListByUser(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(bookingService.getCurrentBookingListByUser(userId), HttpStatus.OK);
    }

    @GetMapping("user/future/{id}")
    public ResponseEntity<List<BookingListItemForUser>> getFutureBookingListByUser(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(bookingService.getFutureBookingListByUser(userId), HttpStatus.OK);
    }

    @GetMapping("user/past/{id}")
    public ResponseEntity<List<BookingListItemForUser>> getPastBookingListByUser(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(bookingService.getPastBookingListByUser(userId), HttpStatus.OK);
    }

}
