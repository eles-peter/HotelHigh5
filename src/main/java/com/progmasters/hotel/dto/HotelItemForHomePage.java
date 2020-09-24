package com.progmasters.hotel.dto;

import com.progmasters.hotel.domain.Hotel;

public class HotelItemForHomePage {

    private Long id;
    private String name;
    private String hotelType;
    private String hotelImageUrl;
    private Integer bestPricePerNightPerPerson;
    private Double avgRate;

    public HotelItemForHomePage() {
    }

    public HotelItemForHomePage(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.hotelType = hotel.getHotelType().getDisplayName();
        if (!hotel.getHotelImageUrls().isEmpty()) {
            this.hotelImageUrl = hotel.getHotelImageUrls().get(0);
        }
        this.avgRate = hotel.getAvgRate();
    }

    public HotelItemForHomePage(Hotel hotel, int bestPricePerNightPerPerson) {
        this(hotel);
        this.setBestPricePerNightPerPerson(bestPricePerNightPerPerson);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public String getHotelImageUrl() {
        return hotelImageUrl;
    }

    public void setHotelImageUrl(String hotelImageUrl) {
        this.hotelImageUrl = hotelImageUrl;
    }

    public Integer getBestPricePerNightPerPerson() {
        return bestPricePerNightPerPerson;
    }

    public void setBestPricePerNightPerPerson(Integer bestPricePerNightPerPerson) {
        this.bestPricePerNightPerPerson = bestPricePerNightPerPerson;
    }

    public Double getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(Double avgRate) {
        this.avgRate = avgRate;
    }
}
