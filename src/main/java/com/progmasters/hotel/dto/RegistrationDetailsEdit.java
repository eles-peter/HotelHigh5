package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Account;

public class RegistrationDetailsEdit {
    private String email;
    private String firstname;
    private String lastname;
    private String address;

    public RegistrationDetailsEdit(Account account) {
        this.email = account.getEmail();
        this.firstname = account.getFirstname();
        this.lastname = account.getLastname();
        this.address = account.getAddress();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
