package com.progmasters.hotel.domain;

import com.progmasters.hotel.dto.RegistrationDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String firstname;
    private String lastname;
    private String address;
    private Long hotelId;
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "guest")
    private List<Booking> bookingList;

    private Boolean isEnabled = false;

    public Account() {
    }

    public Account(RegistrationDetails registrationDetails) {
        this.email = registrationDetails.getEmail();
        this.username = registrationDetails.getEmail();
        this.password = registrationDetails.getPassword();
        this.firstname = registrationDetails.getFirstname();
        this.lastname = registrationDetails.getLastname();
        this.address = registrationDetails.getAddress();
        this.registrationDate = LocalDateTime.now();
    }

    public Boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        this.isEnabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}
