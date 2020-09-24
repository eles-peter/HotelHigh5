import {Component, Input, OnInit} from '@angular/core';
import {BookingService} from "../../../services/booking.service";
import {BookingListItemForHotelModel} from "../../../models/bookingListItemForHotel.model";
import {PopupService} from "../../../services/popup.service";
import {MatDialog} from "@angular/material/dialog";
import {BookingDetailDialogComponent} from "../../booking-detail-dialog/booking-detail-dialog.component";


@Component({
  selector: 'actual-room-bookings',
  templateUrl: './actual-room-bookings.component.html',
  styleUrls: ['./actual-room-bookings.component.css']
})
export class ActualRoomBookingsComponent implements OnInit {

  @Input() roomId: number;
  currentBookingList: BookingListItemForHotelModel[];
  futureBookingList: BookingListItemForHotelModel[];

  constructor(private bookingService: BookingService,
              private popupService: PopupService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    if (this.roomId) {
      this.getCurrentBookingList(this.roomId);
      this.getFutureBookingList(this.roomId);
    }
  }

  getCurrentBookingList(roomId: number) {
    this.bookingService.getCurrentBookingListByRoom(roomId).subscribe(
      (response: BookingListItemForHotelModel[]) => {
        this.currentBookingList = response;
      },
      error => console.warn(error)
    );
  }

  getFutureBookingList(roomId: number) {
    this.bookingService.getFutureBookingListByRoom(roomId).subscribe(
      (response: BookingListItemForHotelModel[]) => {
        this.futureBookingList = response;
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


}
