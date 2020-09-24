package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Account;

public class AccountListItem {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String address;

    public AccountListItem() {
    }

    public AccountListItem(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.firstname = account.getFirstname();
        this.lastname = account.getLastname();
        this.address = account.getAddress();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
