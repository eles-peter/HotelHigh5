package com.progmasters.hotel.security;

import com.progmasters.hotel.domain.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class AuthenticatedLoginDetails {

    private Long id;
    private String name;
    private String role;
    private Long hotelId;
    private String lastname;

    public AuthenticatedLoginDetails() {
    }

    public AuthenticatedLoginDetails(UserDetails user) {
        this.name = user.getUsername();
        this.role = findRole(user);

        this.hotelId = 0L;
        this.id = 0L;
        this.lastname = null;
    }

    public AuthenticatedLoginDetails(Account account) {
        this.id = account.getId();
        this.name = account.getEmail();
        this.hotelId = account.getHotelId();
        this.role = account.getRole().toString();
        this.lastname = account.getLastname();

    }

    private String findRole(UserDetails user) {
        String role = null;
        List<GrantedAuthority> roles = user.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .collect(Collectors.toList());
        if (!roles.isEmpty()) {
            role = roles.get(0).getAuthority();
        }

        return role;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
