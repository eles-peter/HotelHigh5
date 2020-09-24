import {Component, Input, OnInit} from '@angular/core';
import {BookingListItemForHotelModel} from "../../../models/bookingListItemForHotel.model";
import {BookingService} from "../../../services/booking.service";
import {PopupService} from "../../../services/popup.service";
import {MatDialog} from "@angular/material/dialog";
import {BookingDetailDialogComponent} from "../../booking-detail-dialog/booking-detail-dialog.component";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'future-hotel-bookings',
  templateUrl: './future-hotel-bookings.component.html',
  styleUrls: ['./future-hotel-bookings.component.css']
})
export class FutureHotelBookingsComponent implements OnInit {

  @Input() hotelId: BehaviorSubject<number>;
  futureBookingList: BookingListItemForHotelModel[];
  listPageNumber: number = 0;
  fullNumberOfPages: number;
  pageNumbers: number[] = [];
  private id: number;

  constructor(private bookingService: BookingService,
              private popupService: PopupService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.hotelId.subscribe((id) => {
      if (id != 0) {
        this.id = id;
        this.getFutureBookingList(id);
      }
    });
  }

  getFutureBookingList(hotelId: number) {
    this.bookingService.getFutureBookingListByHotel(hotelId, this.listPageNumber).subscribe(
      (response) => {
        this.futureBookingList = response.bookingSubList;
        this.fullNumberOfPages = response.fullNumberOfPages;
      },
      error => console.warn(error)
    );
  }

  bookingDetails(bookingId: number) {
    let dialogRef = this.dialog.open(BookingDetailDialogComponent, {
      height: '600px',
      width: '850px',
      data: bookingId,
    });
    dialogRef.afterClosed().subscribe(
      response => {
        if (response) {
          this.ngOnInit();
        }
      }
    )
  }

  deleteBooking(bookingId: number) {
    this.popupService.openConfirmPopup("Biztos törölni szeretnéd ezt a foglalást?")
      .afterClosed().subscribe(res => {
      if (res) {
        this.bookingService.deleteBooking(bookingId).subscribe(
          () => {
            this.ngOnInit();
          },
          error => console.warn(error),
        );
      }
    })
  }

  setListPageNumber(emittedListPageNumber: number) {
    this.listPageNumber = emittedListPageNumber;
    this.getFutureBookingList(this.id);
  }
}
