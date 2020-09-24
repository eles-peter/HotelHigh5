import {Component, Input, OnInit} from '@angular/core';
import {BookingService} from "../../../services/booking.service";
import {PopupService} from "../../../services/popup.service";
import {MatDialog} from "@angular/material/dialog";
import {BookingDetailDialogComponent} from "../../booking-detail-dialog/booking-detail-dialog.component";
import {BookingListItemForUserModel} from "../../../models/bookingListItemForUser.model";
import {BehaviorSubject} from "rxjs";


@Component({
  selector: 'actual-user-bookings',
  templateUrl: './actual-user-bookings.component.html',
  styleUrls: ['./actual-user-bookings.component.css']
})
export class ActualUserBookingsComponent implements OnInit {

  @Input() userId: BehaviorSubject<number>;
  currentBookingList: BookingListItemForUserModel[];
  futureBookingList: BookingListItemForUserModel[];

  constructor(private bookingService: BookingService,
              private popupService: PopupService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.userId.subscribe((id) => {
      if (id != 0) {
        this.getCurrentBookingList(id);
        this.getFutureBookingList(id);
      }
    });
  }

  getCurrentBookingList(userId: number) {
    this.bookingService.getCurrentBookingListByUser(userId).subscribe(
      (response: BookingListItemForUserModel[]) => {
        this.currentBookingList = response;
      },
      error => console.warn(error)
    );
  }

  getFutureBookingList(userId: number) {
    this.bookingService.getFutureBookingListByUser(userId).subscribe(
      (response: BookingListItemForUserModel[]) => {
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
