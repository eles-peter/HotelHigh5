package com.progmasters.hotel.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.progmasters.hotel.domain.Account;
import com.progmasters.hotel.domain.HotelFeatureType;
import com.progmasters.hotel.dto.*;
import com.progmasters.hotel.service.AccountService;
import com.progmasters.hotel.service.HotelService;
import com.progmasters.hotel.service.RoomReservationService;
import com.progmasters.hotel.service.RoomService;
import com.progmasters.hotel.validator.HotelCreateItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

	private static final int NUM_OF_ELEMENTS_PER_PAGE = 10;
	private static final int NUM_OF_ELEMENTS_ON_HOMEPAGE = 4;

	private HotelService hotelService;
	private RoomReservationService roomReservationService;
	private RoomService roomService;
	private HotelCreateItemValidator validator;
	private AccountService accountService;

	@Autowired
	public HotelController(HotelService hotelService, RoomReservationService roomReservationService, RoomService roomService, HotelCreateItemValidator validator, AccountService accountService) {
		this.hotelService = hotelService;
		this.roomReservationService = roomReservationService;
		this.roomService = roomService;
		this.validator = validator;
		this.accountService = accountService;
	}

    @InitBinder("HotelCreateItem")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

	@GetMapping("/formData")
	public ResponseEntity<HotelFormData> getHotelFormData() {
		List<HotelTypeOption> hotelTypeOptionList = this.hotelService.getHotelTypeOptionList();
		List<HotelFeatureTypeOption> hotelFeatureTypeOptionList = this.hotelService.getHotelFeatureTypeOptionList();
		HotelFormData hotelFormData = new HotelFormData(hotelTypeOptionList, hotelFeatureTypeOptionList);
		return new ResponseEntity<>(hotelFormData, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Long> saveHotel(@Valid @RequestBody HotelCreateItem hotelCreateItem) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user = (UserDetails) authentication.getPrincipal();
		Long hotelId = hotelService.saveHotel(hotelCreateItem);
		Account account = accountService.findByUsername(user.getUsername());
		account.setHotelId(hotelId);
		accountService.saveConfirmedAccount(account);
		return new ResponseEntity<>(hotelId, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<HotelListItemSubList> getHotelList(@RequestParam(required = false) Integer listPageNumber) {
		if (listPageNumber == null) listPageNumber = 0;
		return new ResponseEntity<>(hotelService.getPageOfHotelListOrderByBestPrice(listPageNumber, NUM_OF_ELEMENTS_PER_PAGE), HttpStatus.OK);
	}

	@GetMapping("/{id}/filter")
	public ResponseEntity<HotelDetailItem> getFilteredHotel(
			@PathVariable Long id,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return new ResponseEntity<>(hotelService.getFilteredHotelDetailItem(id, startDate, endDate), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<HotelDetailItem> getHotel(@PathVariable Long id) {
		return new ResponseEntity<>(hotelService.getHotelDetailItem(id), HttpStatus.OK);
	}

	@GetMapping("/short/{id}")
	public ResponseEntity<HotelShortItem> getHotelShortItem(@PathVariable Long id) {
		return new ResponseEntity<>(hotelService.getHotelShortItem(id), HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<HotelListItemSubList> getFilteredHotelList(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam("numberOfGuests") long numberOfGuests,
			@RequestParam(required = false) List<String> hotelFeatures,
			@RequestParam(required = false) Integer listPageNumber) {
		if (listPageNumber == null) listPageNumber = 0;
		HotelListItemSubList hotelList;
		if (hotelFeatures == null || hotelFeatures.isEmpty()) {
			hotelList = hotelService.getPageOfHotelListFilteredByDateAndPerson(startDate, endDate, numberOfGuests, listPageNumber, NUM_OF_ELEMENTS_PER_PAGE);
		} else {
			List<HotelFeatureType> hotelFeatureEnumList = hotelFeatures.stream().map(HotelFeatureType::valueOf).collect(Collectors.toList());
			hotelList = hotelService.getPageOfHotelListFilteredByDatePersonAndFeatures(startDate, endDate, numberOfGuests, hotelFeatureEnumList, listPageNumber, NUM_OF_ELEMENTS_PER_PAGE);
		}
		return new ResponseEntity<>(hotelList, HttpStatus.OK);
	}

	@GetMapping("/bestprice")
	public ResponseEntity<List<HotelItemForHomePage>> getHotelListTheBestPriceForHomePage() {
		return new ResponseEntity<>(hotelService.getHotelListTheBestPriceForHomePage(NUM_OF_ELEMENTS_ON_HOMEPAGE), HttpStatus.OK);
	}

	@GetMapping("/bestavgrate")
	public ResponseEntity<List<HotelItemForHomePage>> getHotelListTheBestAvgRateForHomePage() {
		return new ResponseEntity<>(hotelService.getHotelListTheBestAvgRateForHomePage(NUM_OF_ELEMENTS_ON_HOMEPAGE), HttpStatus.OK);
	}

	@GetMapping("/random")
	public ResponseEntity<List<HotelItemForHomePage>> getRandomHotelListForHomePage() {
		return new ResponseEntity<>(hotelService.getRandomHotelListForHomePage(NUM_OF_ELEMENTS_ON_HOMEPAGE), HttpStatus.OK);
	}

	// TODO check if hotel has rooms

	@DeleteMapping("/{id}")
	public ResponseEntity<List<HotelListItem>> deleteHotel(@PathVariable Long id) {
		boolean isDeleteSuccessful = hotelService.deleteHotel(id);
		ResponseEntity<List<HotelListItem>> result;
		if (isDeleteSuccessful) {
			result = new ResponseEntity<>(hotelService.getHotelListItemList(), HttpStatus.OK);
		} else {
			result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
 		return result;
	}

	@GetMapping("/formData/{id}")
	public ResponseEntity<HotelCreateItem> getHotelForUpdate(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<>(hotelService.getHotelCreateItem(id), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateHotel(@Valid @RequestBody HotelCreateItem hotelCreateItem, @PathVariable Long id) {
		boolean hotelIsUpdated = hotelService.updateHotel(hotelCreateItem, id);
		return hotelIsUpdated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/uploadImage/{id}")
	public ResponseEntity<Void> uploadHotelImage(@RequestParam MultipartFile file, @PathVariable Long id) {
		hotelService.saveHotelImage(file, id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/deleteImage/{id}")
	public ResponseEntity<Void> deleteImageFromHotel(@RequestParam String imageURL,@PathVariable Long id){
    	hotelService.deleteHotelImage(imageURL,id);
    	return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/images/{id}")
	public ResponseEntity<List<String>> getHotelImages(@PathVariable Long id){
		List<String> imageURLs = hotelService.getHotelImages(id);
		return new ResponseEntity<>(imageURLs,HttpStatus.OK);
	}

}
