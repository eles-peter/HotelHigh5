package com.progmasters.hotel.domain;

import com.progmasters.hotel.dto.BookingCreateItem;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guest")
    private Account guest;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @Column(name = "date_of_booking")
    private LocalDateTime dateOfBooking;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "booking")
    private List<RoomReservation> roomReservations = new ArrayList<>();

    @Column(name = "number_of_guests")
    private Integer numberOfGuests;

    @Column(name = "full_price")
    private Double priceOfBooking;

    Booking() {
    }

    public Booking(BookingCreateItem bookingCreateItem) {
        this.remark = bookingCreateItem.getRemark();
        this.numberOfGuests = bookingCreateItem.getNumberOfGuests();
        this.dateOfBooking = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Account getGuest() {
        return guest;
    }

    public String getRemark() {
        return remark;
    }

    public LocalDateTime getDateOfBooking() {
        return dateOfBooking;
    }

    public List<RoomReservation> getRoomReservations() {
        return roomReservations;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Double getPriceOfBooking() {
        return priceOfBooking;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setDateOfBooking(LocalDateTime dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGuest(Account guest) {
        this.guest = guest;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setRoomReservations(List<RoomReservation> roomReservations) {
        this.roomReservations = roomReservations;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setPriceOfBooking(Double priceOfBooking) {
        this.priceOfBooking = priceOfBooking;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
