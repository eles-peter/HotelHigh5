package com.progmasters.hotel.dto;

import java.util.List;

public class BookingSubListForHotel {

    private List<BookingListItemForHotel> bookingSubList;
    private Integer listPageNumber;
    private Integer fullNumberOfPages;

    public BookingSubListForHotel() {
    }

    public BookingSubListForHotel(List<BookingListItemForHotel> bookingSubList, Integer listPageNumber, Integer fullNumberOfPages) {
        this.bookingSubList = bookingSubList;
        this.listPageNumber = listPageNumber;
        this.fullNumberOfPages = fullNumberOfPages;
    }

    public List<BookingListItemForHotel> getBookingSubList() {
        return bookingSubList;
    }

    public void setBookingSubList(List<BookingListItemForHotel> bookingSubList) {
        this.bookingSubList = bookingSubList;
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
