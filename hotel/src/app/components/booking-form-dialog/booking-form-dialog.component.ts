import {Component, Inject, OnInit, ViewEncapsulation} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BookingCreateItemModel} from "../../models/bookingCreateItem.model";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {BookingService} from "../../services/booking.service";
import {BookingDetailsModel} from "../../models/bookingDetails.model";
import {LoginService} from "../../services/login.service";
import {Router} from "@angular/router";
import {RoomReservationDetailsModel} from "../../models/roomReservationDetails.model";
import {RoomReservationShortItemModel} from "../../models/roomReservationShortItem.model";

@Component({
  selector: 'app-booking-form-dialog',
  templateUrl: './booking-form-dialog.component.html',
  styleUrls: ['./booking-form-dialog.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class BookingFormDialogComponent implements OnInit {

  userBookingForm: FormGroup;
  hotelOwnerBookingForm: FormGroup;
  priceOfBooking: number;
  bookingDetails: BookingDetailsModel;
  bookingStatus: string = 'userBooking';
  username: string;

  constructor(public dialogRef: MatDialogRef<BookingFormDialogComponent>,
              private bookingService: BookingService,
              private loginService: LoginService,
              private router: Router,
              @Inject(MAT_DIALOG_DATA) public data) {
    this.userBookingForm = new FormGroup({
        'remark': new FormControl(''),
      'aSZF': new FormControl(false, Validators.requiredTrue),
      }
    );
    this.hotelOwnerBookingForm = new FormGroup({
      'firstname': new FormControl('', Validators.required),
      'lastname': new FormControl('', Validators.required),
      'address': new FormControl('', Validators.required),
      'email': new FormControl('', [Validators.required, Validators.email]),
      'numberOfGuests': new FormControl('', [Validators.min(1), Validators.required]),
      'hotelOwnerRemark': new FormControl(''),
    });
  }

  ngOnInit(): void {
    this.loginService.authenticatedLoginDetailsModel.subscribe(
      (response) => {
        if (response !== null) {
          if (response.role === "ROLE_USER") {
            this.username = response.name;
          } else if (response.role === "ROLE_HOTELOWNER") {
            this.bookingStatus = 'hotelOwnerBooking';
          }
        } else {
          this.router.navigate([''])
        }
      });

    this.priceOfBooking = 0;
    this.data.roomReservationList.forEach((roomReservation: RoomReservationDetailsModel) => {
        this.priceOfBooking += roomReservation.numberOfNights * roomReservation.room.pricePerNight;
      }
    );
  }

  closeDialog(dialogResult) {
    this.dialogRef.close(dialogResult);
  }

  onSubmitByUser() {
    const input = {...this.userBookingForm.value};
    const bookingData: BookingCreateItemModel = this.createBookingDataForUser(input);
    this.bookingService.createBooking(bookingData).subscribe(
      (bookingId: number) => {
        this.bookingService.bookingDetail(bookingId).subscribe(
          (response: BookingDetailsModel) => {
            this.bookingStatus = 'created';
            this.bookingDetails = this.parseDate(response);
          }, error => {
            this.bookingStatus = 'failed';
          }
        )
      }, error => {
        this.bookingStatus = 'failed';
      },
    );
  }

  onSubmitByHotelOwner() {
    const input = {...this.hotelOwnerBookingForm.value};
    const bookingData: BookingCreateItemModel = this.createBookingDataForHotelOwner(input);
    this.bookingService.createBooking(bookingData).subscribe(
      (bookingId: number) => {
        this.bookingService.bookingDetail(bookingId).subscribe(
          (response: BookingDetailsModel) => {
            this.bookingStatus = 'created';
            this.bookingDetails = this.parseDate(response);
          }, error => {
            this.bookingStatus = 'failed';
          }
        )
      }, error => {
        this.bookingStatus = 'failed';
      },
    );
  }

  createBookingDataForHotelOwner = (input): BookingCreateItemModel => {
    return {
      firstName: input.firstname,
      lastName: input.lastname,
      address: input.address,
      email: input.email,
      remark: input.hotelOwnerRemark,
      numberOfGuests: input.numberOfGuests,
      roomReservationList: this.createRoomReservationShortItemList(),
    }
  }

  createBookingDataForUser = (input): BookingCreateItemModel => {
    return {
      guestAccountName: this.username,
      remark: input.remark,
      numberOfGuests: this.data.numberOfGuests,
      roomReservationList: this.createRoomReservationShortItemList(),
    }
  };

  createRoomReservationShortItemList() {
    let roomReservationList: RoomReservationShortItemModel[] = [];
    this.data.roomReservationList.forEach(roomReservation => {
      roomReservationList.push({
        startDate: roomReservation.startDate,
        endDate: roomReservation.endDate,
        roomId: roomReservation.room.id,
      });
    });
    return roomReservationList;
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
