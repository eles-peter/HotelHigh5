import {Component, Input, OnInit} from '@angular/core';
import {BookingListItemForHotelModel} from "../../../models/bookingListItemForHotel.model";
import {BookingService} from "../../../services/booking.service";
import {PopupService} from "../../../services/popup.service";
import {MatDialog} from "@angular/material/dialog";
import {BookingDetailDialogComponent} from "../../booking-detail-dialog/booking-detail-dialog.component";

@Component({
  selector: 'past-room-bookings',
  templateUrl: './past-room-bookings.component.html',
  styleUrls: ['./past-room-bookings.component.css']
})
export class PastRoomBookingsComponent implements OnInit {

  @Input() roomId: number;
  pastBookingList: BookingListItemForHotelModel[];

  constructor(private bookingService: BookingService,
              private popupService: PopupService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    if (this.roomId) {
      this.getPastBookingList(this.roomId);
    }
  }

  getPastBookingList(roomId: number) {
    this.bookingService.getPastBookingListByRoom(roomId).subscribe(
      (response: BookingListItemForHotelModel[]) => {
        this.pastBookingList = response;
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
