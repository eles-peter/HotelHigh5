import {Component, Inject, OnInit, ViewEncapsulation} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BookingDetailsModel} from "../../models/bookingDetails.model";
import {BookingService} from "../../services/booking.service";
import {PopupService} from "../../services/popup.service";
import {RoomReservationService} from "../../services/roomReservation.service";

@Component({
  selector: 'app-booking-detail-dialog',
  templateUrl: './booking-detail-dialog.component.html',
  styleUrls: ['./booking-detail-dialog.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class BookingDetailDialogComponent implements OnInit {

  bookingDetails: BookingDetailsModel;
  closeDialogResult: boolean = false;

  constructor(public dialogRef: MatDialogRef<BookingDetailDialogComponent>,
              private bookingService: BookingService,
              private roomReservationService: RoomReservationService,
              private popupService: PopupService,
              @Inject(MAT_DIALOG_DATA) public data) {
  }

  ngOnInit(): void {
    this.bookingService.bookingDetail(this.data).subscribe(
      (response: BookingDetailsModel) => {
        this.bookingDetails = this.parseDate(response);
      }, error => {
        console.log(error);
      }
    );
  }

  closeDialog(closeDialogResult) {
    if (this.closeDialogResult) closeDialogResult = true;
    this.dialogRef.close(closeDialogResult);
  }

  deleteBooking() {
    this.popupService.openConfirmPopup("Biztos törölni szeretnéd a teljes foglalást?")
      .afterClosed().subscribe(response => {
      if (response) {
        this.bookingService.deleteBooking(this.bookingDetails.id).subscribe(
          () => {
            this.closeDialog(true);
          },
          error => console.warn(error),
        );
      }
    })
  }

  deleteRoomReservation(roomReservationId) {
    if (this.bookingDetails.roomReservationList.length > 1) {
      this.popupService.openConfirmPopup("Biztos törölni szeretnéd ezt a szobafoglalást?")
        .afterClosed().subscribe(response => {
        if (response) {
          this.roomReservationService.deleteRoomReservation(roomReservationId).subscribe(
            () => {
              this.ngOnInit();
              this.closeDialogResult = true;
            },
            error => console.warn(error),
          );
        }
      })
    } else {
      this.deleteBooking()
    }
  }

  private parseDate(response: BookingDetailsModel): BookingDetailsModel {
    response.roomReservationList.forEach(roomReservation => {
      roomReservation.startDate = new Date(roomReservation.startDate);
      roomReservation.startDate.setHours(0, 0, 0, 0);
      roomReservation.endDate = new Date(roomReservation.endDate);
      roomReservation.endDate.setHours(0, 0, 0, 0);
    });
    return response;
  }
}
