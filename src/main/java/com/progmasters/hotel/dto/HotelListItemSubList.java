package com.progmasters.hotel.dto;

import java.util.List;

public class HotelListItemSubList {

    private List<HotelListItem> hotelSubList;
    private Integer listPageNumber;
    private Integer fullNumberOfPages;

    public HotelListItemSubList() {
    }

    public HotelListItemSubList(List<HotelListItem> hotelSubList, Integer listPageNumber, Integer fullNumberOfPages) {
        this.hotelSubList = hotelSubList;
        this.listPageNumber = listPageNumber;
        this.fullNumberOfPages = fullNumberOfPages;
    }

    public List<HotelListItem> getHotelSubList() {
        return hotelSubList;
    }

    public void setHotelSubList(List<HotelListItem> hotelSubList) {
        this.hotelSubList = hotelSubList;
    }

    public Integer getListPageNumber() {
        return listPageNumber;
    }

    public void setListPageNumber(Integer listPageNumber) {
        this.listPageNumber = listPageNumber;
    }

    public Integer getFullNumberOfPages() {
        return fullNumberOfPages;
    }

    public void setFullNumberOfPages(Integer fullNumberOfPages) {
        this.fullNumberOfPages = fullNumberOfPages;
    }
}


