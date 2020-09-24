package com.progmasters.hotel.service;

import com.progmasters.hotel.domain.*;
import com.progmasters.hotel.dto.*;
import com.progmasters.hotel.repository.AccountRepository;
import com.progmasters.hotel.repository.BookingRepository;
import com.progmasters.hotel.repository.RoomRepository;
import com.progmasters.hotel.repository.RoomReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomReservationRepository roomReservationRepository;
    private final RoomRepository roomRepository;
    private final AccountRepository accountRepository;
    private static final ZoneId HOTELS_ZONEID = ZoneId.of("Europe/Budapest");
    private final EmailService emailService;

    @Autowired
    public BookingService(RoomReservationRepository roomReservationRepository, BookingRepository bookingRepository, RoomRepository roomRepository, AccountRepository accountRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.roomReservationRepository = roomReservationRepository;
        this.roomRepository = roomRepository;
        this.accountRepository = accountRepository;
        this.emailService = emailService;
    }

    public Long saveBooking(BookingCreateItem bookingCreateItem) {
        Booking booking = new Booking(bookingCreateItem);

        //Check the rooms are in the same hotel
        Long hotelId = getHotelIdAndValidate(bookingCreateItem);
        if (hotelId == null) return null;

        //Check the time: later than now, end is later then start (and not equal)
        if (!validateReservationTime(bookingCreateItem)) return null;

        //Check the rooms are free and exist (and create RoomReservation List)
        List<RoomReservation> roomReservations = getRoomReservationsAndValidate(bookingCreateItem, booking);
        if (roomReservations.isEmpty()) return null;

        double priceOfBooking = getPriceOfBooking(bookingCreateItem);
        booking.setPriceOfBooking(priceOfBooking);

        //Check and set guest or guest data
        if (!setGuestOrGuestDataToBooking(bookingCreateItem, booking)) return null;

        roomReservations.forEach(this.roomReservationRepository::save);
        this.bookingRepository.save(booking);

        if (booking.getGuest() != null) {
            emailService.sendMailAtBooking(booking.getGuest());
        } else {
            emailService.sendMailAtBookingByHotelOwner(booking.getEmail(), booking.getFirstName(), booking.getLastName());
        }


        return booking.getId();
    }

    private boolean setGuestOrGuestDataToBooking(BookingCreateItem bookingCreateItem, Booking booking) {
        if (bookingCreateItem.getGuestAccountName() != null) {
            Account guestAccount = this.accountRepository.findByUsername(bookingCreateItem.getGuestAccountName());
            if (guestAccount == null || !guestAccount.getRole().equals(Role.ROLE_USER)) return false;
            booking.setGuest(guestAccount);
        } else {
            if (bookingCreateItem.getFirstName() != null) booking.setFirstName(bookingCreateItem.getFirstName());
            else return false;
            if (bookingCreateItem.getLastName() != null) booking.setLastName(bookingCreateItem.getLastName());
            else return false;
            if (bookingCreateItem.getAddress() != null) booking.setAddress(bookingCreateItem.getAddress());
            else return false;
            if (bookingCreateItem.getEmail() != null) booking.setEmail(bookingCreateItem.getEmail());
            else return false;
        }
        return true;
    }

    public boolean deleteBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            bookingRepository.delete(booking);
            emailService.sendMailAtDeleteBooking(booking.getGuest());
            return true;
        } else {
            return false;
        }
    }

    public BookingDetails getBookingDetails(Long bookingId) {
        BookingDetails bookingDetails;
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            bookingDetails = new BookingDetails(optionalBooking.get());
        } else {
            throw new IllegalArgumentException("There is no Booking for this id:" + bookingId);
        }
        return bookingDetails;
    }

    public List<BookingListItemForHotel> getBookingListByRoom(Long roomId) {
        return bookingRepository.findAllByRoomId(roomId).stream().map(BookingListItemForHotel::new).collect(Collectors.toList());
    }

    public List<BookingListItemForHotel> getCurrentBookingListByRoom(Long roomId) {
        return bookingRepository.findCurrentByRoomId(roomId, LocalDate.now(HOTELS_ZONEID))
                .stream().map(BookingListItemForHotel::new).collect(Collectors.toList());
    }

    public BookingSubListForHotel getFutureBookingListByRoom
            (Long roomId, Integer listPageNumber, Integer numOfElementsPerPage) {
        Pageable pageable = PageRequest.of(listPageNumber, numOfElementsPerPage, Sort.by("reservations.startDate"));
        Page<Booking> bookingPage = bookingRepository.findFutureByRoomId(roomId, LocalDate.now(HOTELS_ZONEID), pageable);
        List<BookingListItemForHotel> bookingList = bookingPage.stream().map(BookingListItemForHotel::new).collect(Collectors.toList());
        return new BookingSubListForHotel(bookingList, bookingPage.getNumber(), bookingPage.getTotalPages());
    }

    public BookingSubListForHotel getPastBookingListByRoom
            (Long roomId, Integer listPageNumber, Integer numOfElementsPerPage) {
        Pageable pageable = PageRequest.of(listPageNumber, numOfElementsPerPage, Sort.by("reservations.startDate").descending());
        Page<Booking> bookingPage = bookingRepository.findPastByRoomId(roomId, LocalDate.now(HOTELS_ZONEID), pageable);
        List<BookingListItemForHotel> bookingList = bookingPage.stream().map(BookingListItemForHotel::new).collect(Collectors.toList());
        return new BookingSubListForHotel(bookingList, bookingPage.getNumber(), bookingPage.getTotalPages());
    }

    public List<BookingListItemForHotel> getBookingListByHotel(Long hotelId) {
        return bookingRepository.findAllByHotelId(hotelId).stream().map(BookingListItemForHotel::new).collect(Collectors.toList());
    }

    public BookingSubListForHotel getCurrentBookingListByHotel
            (Long hotelId, Integer listPageNumber, Integer numOfElementsPerPage) {
        Pageable pageable = PageRequest.of(listPageNumber, numOfElementsPerPage, Sort.by("reservations.startDate"));
        Page<BookingRepository.BookingResult> queryResults = bookingRepository.findCurrentByHotelId(hotelId, LocalDate.now(HOTELS_ZONEID), pageable);
        return getBookingListItemForHotels(queryResults);
    }

    public BookingSubListForHotel getFutureBookingListByHotel
            (Long hotelId, Integer listPageNumber, Integer numOfElementsPerPage) {
        Pageable pageable = PageRequest.of(listPageNumber, numOfElementsPerPage, Sort.by("reservations.startDate"));
        Page<BookingRepository.BookingResult> queryResults = bookingRepository.findFutureByHotelId(hotelId, LocalDate.now(HOTELS_ZONEID), pageable);
        return getBookingListItemForHotels(queryResults);
    }

    public BookingSubListForHotel getPastBookingListByHotel
            (Long hotelId, Integer listPageNumber, Integer numOfElementsPerPage) {
        Pageable pageable = PageRequest.of(listPageNumber, numOfElementsPerPage, Sort.by("reservations.startDate").descending());
        Page<BookingRepository.BookingResult> queryResults = bookingRepository.findPastByHotelId(hotelId, LocalDate.now(HOTELS_ZONEID), pageable);
        return getBookingListItemForHotels(queryResults);
    }

    private BookingSubListForHotel getBookingListItemForHotels(Page<BookingRepository.BookingResult> queryResults) {
        if (!queryResults.isEmpty()) {
            List<BookingListItemForHotel> bookingList = new ArrayList<>();
            for (BookingRepository.BookingResult bookingResult : queryResults) {
                BookingListItemForHotel bookingListItemForHotel = new BookingListItemForHotel(bookingResult.getResultBooking());
                bookingList.add(bookingListItemForHotel);
            }
            return new BookingSubListForHotel(bookingList, queryResults.getNumber(), queryResults.getTotalPages());
        } else return null;
    }


    public List<BookingListItemForUser> getBookingListByUser(Long userId) {
        return bookingRepository.findAllByUserId(userId).stream().map(BookingListItemForUser::new).collect(Collectors.toList());
    }

    public List<BookingListItemForUser> getCurrentBookingListByUser(Long userId) {
        return bookingRepository.findCurrentByUserId(userId, LocalDate.now(HOTELS_ZONEID))
                .stream().map(BookingListItemForUser::new).collect(Collectors.toList());
    }

    public List<BookingListItemForUser> getFutureBookingListByUser(Long userId) {
        return bookingRepository.findFutureByUserId(userId, LocalDate.now(HOTELS_ZONEID))
                .stream().map(BookingListItemForUser::new).collect(Collectors.toList());
    }

    public List<BookingListItemForUser> getPastBookingListByUser(Long userId) {
        return bookingRepository.findPastByUserId(userId, LocalDate.now(HOTELS_ZONEID))
                .stream().map(BookingListItemForUser::new).collect(Collectors.toList());
    }

    private List<RoomReservation> getRoomReservationsAndValidate(BookingCreateItem bookingCreateItem, Booking booking) {
        List<RoomReservation> roomReservations = new ArrayList<>();
        for (RoomReservationShortItem roomReservationShortItem : bookingCreateItem.getRoomReservationList()) {
            Optional<Room> optionalRoom = this.roomRepository.findById(roomReservationShortItem.getRoomId());
            if (optionalRoom.isPresent()
                    && isRoomFree(roomReservationShortItem.getRoomId(), roomReservationShortItem.getStartDate(), roomReservationShortItem.getEndDate())) {
                RoomReservation roomReservation = new RoomReservation(roomReservationShortItem);
                roomReservation.setRoom(optionalRoom.get());
                roomReservation.setBooking(booking);
                roomReservations.add(roomReservation);
            } else {
                return new ArrayList<>();
            }
        }
        return roomReservations;
    }

    public boolean isRoomFree(Long roomId, LocalDate startDate, LocalDate enDate) {
        List<RoomReservation> roomReservations =
                this.roomReservationRepository.findAllByRoomIdAndEndDateAfterAndStartDateBefore(roomId, startDate, enDate);
        return roomReservations.isEmpty();
    }

    private boolean validateReservationTime(BookingCreateItem bookingCreateItem) {
        boolean reservationTimeIsValid = true;
        for (RoomReservationShortItem roomReservation : bookingCreateItem.getRoomReservationList()) {
            Long numberOfNights = DAYS.between(roomReservation.getStartDate(), roomReservation.getEndDate());
            if (numberOfNights < 1) reservationTimeIsValid = false;
            if (roomReservation.getStartDate().isBefore(LocalDate.now())) reservationTimeIsValid = false;
        }
        return reservationTimeIsValid;
    }

    private Long getHotelIdAndValidate(BookingCreateItem bookingCreateItem) {
        Long hotelId = null;
        for (RoomReservationShortItem roomReservation : bookingCreateItem.getRoomReservationList()) {
            Optional<Room> optionalRoom = this.roomRepository.findById(roomReservation.getRoomId());
            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();
                Long actualHotelId = room.getHotel().getId();
                if (hotelId != null && !actualHotelId.equals(hotelId)) {
                    return null;
                } else {
                    hotelId = actualHotelId;
                }
            }
        }
        return hotelId;
    }

    private double getPriceOfBooking(BookingCreateItem bookingCreateItem) {
        double priceOfBooking = 0.0;
        for (RoomReservationShortItem roomReservation : bookingCreateItem.getRoomReservationList()) {
            Long numberOfNights = DAYS.between(roomReservation.getStartDate(), roomReservation.getEndDate());
            Optional<Room> optionalRoom = this.roomRepository.findById(roomReservation.getRoomId());
            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();
                priceOfBooking += room.getPricePerNight() * numberOfNights;
            }
        }
        return priceOfBooking;
    }

}
