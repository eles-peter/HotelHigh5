import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HotelService} from "../../services/hotel.service";
import {HotelDetailsModel} from "../../models/hotelDetails.model";
import {RoomService} from "../../services/room.service";
import {LoginService} from "../../services/login.service";
import {RoomListItemModel} from "../../models/roomListItem.model";
import {PopupService} from "../../services/popup.service";
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {BookingService} from "../../services/booking.service";
import {BookingFormDialogComponent} from "../booking-form-dialog/booking-form-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {RoomShortListItemModel} from "../../models/roomShortListItem.model";
import {RoomFormDataModel} from "../../models/roomFormData.model";
import {RoomFeatureTypeOptionModel} from "../../models/roomFeatureTypeOption.model";
import {getPublicId} from "../../utils/cloudinaryPublicIdHandler";
import {AuthenticatedLoginDetailsModel} from "../../models/authenticatedLoginDetails.model";
import {LoginComponent} from "../account/login/login.component";
import {NotificationService} from "../../services/notification.service";
import {RoomReservationDetailsModel} from "../../models/roomReservationDetails.model";
import {RouterExtService} from "../../services/routerExtService";
import {Location} from "@angular/common";
import {dateToJsonDateString} from "../../utils/dateUtils";


@Component({
  selector: 'app-hotel-detail',
  templateUrl: './hotel-details.component.html',
  styleUrls: ['./hotel-details.component.css']
})
export class HotelDetailsComponent implements OnInit {

  hotel: HotelDetailsModel;
  priceOfBooking: number;
  maxNumberOfGuest: number = 0;
  hotelIdFromLogin: number;
  hotelIdFromRoute: string;

  bookingForm: FormGroup;
  filterForm: FormGroup;
  dateFilter = (date: Date) =>
    new Date(date.setHours(0, 0, 0, 0)).getTime() >= new Date().setHours(0, 0, 0, 0);
  roomFeatureTypeOption: RoomFeatureTypeOptionModel[];
  account: AuthenticatedLoginDetailsModel;

  isLoggedIn: boolean = false;
  filterData = null;

  constructor(private  hotelService: HotelService, private roomService: RoomService,
              private bookingService: BookingService, private loginService: LoginService,
              private route: ActivatedRoute, private router: Router, private notificationService: NotificationService,
              private popupService: PopupService, private dialog: MatDialog,
              private location: Location,) {

    this.bookingForm = new FormGroup({
      'numberOfGuests': new FormControl(null,
        [Validators.required,
          Validators.min(1)]),
      'bookingDateRange': new FormControl(null,
        Validators.required),
      'roomIdList': new FormArray([],
        this.checkBoxValidator),
    });
    this.filterForm = new FormGroup({
      'roomFeatures': new FormArray([]),
    })
  }

  ngOnInit(): void {
    this.loginService.authenticatedLoginDetailsModel.subscribe(
      (account) => {
        if (account) {
          this.account = account;
          this.isLoggedIn = true;
          this.showHotel();
        } else {
          if (this.router.isActive('/hotel', false) || this.router.isActive('/admin/hotel', false)) {
            this.loginService.checkSession().subscribe((response) => {
              if (response != this.account) {
                this.isLoggedIn = true;
                this.loginService.authenticatedLoginDetailsModel.next(response);
                this.account = response;
              }
              this.showHotel();
            })
          }
        }
      }
    )
  }

  showHotel() {
    if (this.account && this.account.role === "ROLE_HOTELOWNER") {
      this.hotelIdFromLogin = this.account.hotelId;
      if (this.hotelIdFromLogin) {
        this.getHotelDetail(String(this.hotelIdFromLogin))
      } else {
        this.router.navigate(['admin/hotel-create'])
      }
    } else {
      this.route.paramMap.subscribe(
        paramMap => {
          const paramMapId = paramMap.get('id');
          if (paramMapId) {
            this.hotelIdFromRoute = paramMapId;

            if (this.router.url.includes('filter?')) {

              this.route.queryParams.subscribe(
                queryParams => {
                  this.filterData = {
                    numberOfGuests: queryParams['numberOfGuests'],
                    startDate: queryParams['startDate'],
                    endDate: queryParams['endDate'],
                  };

                  this.bookingForm.controls['numberOfGuests'].setValue(this.filterData.numberOfGuests);

                  let filterBookingDateRange = {
                    begin: new Date(this.filterData.startDate),
                    end: new Date(this.filterData.endDate),
                  };
                  this.bookingForm.controls['bookingDateRange'].setValue(filterBookingDateRange);

                  this.getFilteredHotelDetail(this.hotelIdFromRoute, this.filterData);
                }
              );
            } else {
              this.getHotelDetail(this.hotelIdFromRoute);
            }
          }
        });
    }

    this.roomService.getRoomFormData().subscribe(
      (roomFormData: RoomFormDataModel) => {
        this.roomFeatureTypeOption = roomFormData.roomFeatures;
        this.createRoomFeaturesCheckboxControl();
      });
  }

  getHotelDetail = (hotelId: string) => {
    this.hotelService.hotelDetail(hotelId).subscribe(
      (response: HotelDetailsModel) => {
        this.hotel = response;
        this.createRoomBookingFormArray();
      }
    );
  };

  private getFilteredHotelDetail(hotelId: string, filterData: any) {
    this.hotelService.filteredHotelDetail(hotelId, filterData).subscribe(
      (response: HotelDetailsModel) => {
        this.hotel = response;
        this.createRoomBookingFormArray();
      }
    );
  }

  getFilteredRoomList = () => {
    const data = {
      startDate: this.bookingForm.value.bookingDateRange.begin,
      endDate: this.bookingForm.value.bookingDateRange.end,
      roomFeatures: this.createRoomFeaturesFilterArrayToSend(),
    };
    if (data.startDate && data.endDate) {
      this.actualizeURL();
      this.roomService.getFilteredRoomList(this.hotel.id, data).subscribe(
        (response: RoomListItemModel[]) => {
          this.hotel.rooms = response;
          this.clearRoomBookingFormArray();
          this.createRoomBookingFormArray();
          this.priceOfBooking = null;
          this.maxNumberOfGuest = 0;
        },
        error => console.warn(error),
      );
    }
  };

  actualizeURL() {
    let numberOfGuests = this.bookingForm.value.numberOfGuests;
    let startDate = dateToJsonDateString(this.bookingForm.value.bookingDateRange.begin);
    let endDate = dateToJsonDateString(this.bookingForm.value.bookingDateRange.end);
    let newURL = 'hotel/' + this.hotelIdFromRoute + '/filter?numberOfGuests=' + numberOfGuests + '&startDate=' + startDate + '&endDate=' + endDate;
    this.location.replaceState(newURL);
  }

  resetFilters() {
    this.filterForm.reset();
    //TODO resetelni a naptárat!!!
    this.getFilteredRoomList();
  }

  getPriceOfBookingAndMaxCapacity() {
    let numberOfNights: number = 0;
    let roomsPricePerNight = 0;
    let maxCapacity = 0;
    this.bookingForm.value.roomIdList.forEach((value, index) => {
      if (value) {
        roomsPricePerNight += this.hotel.rooms[index].pricePerNight;
        maxCapacity += this.hotel.rooms[index].numberOfBeds;
      }
    });

    if (this.bookingForm.value.bookingDateRange) {
      numberOfNights =
        Math.round((this.bookingForm.value.bookingDateRange.end.getTime() - this.bookingForm.value.bookingDateRange.begin.getTime()) / 86400000);
    }
    this.maxNumberOfGuest = maxCapacity;
    this.priceOfBooking = numberOfNights * roomsPricePerNight;
  }

  createRoomInHotel() {
    this.router.navigate(['/admin/hotel/create-room'])
  }

  updateHotel() {
    this.router.navigate(['/admin/hotel-update'])
  }

  //TODO lechekkolni, hogy van-e a szobához elkövetkező foglalás!!
  deleteRoom(id: number) {
    this.popupService.openConfirmPopup("Biztos törölni szeretnéd a tételt?")
      .afterClosed().subscribe(res => {
      if (res) {
        this.roomService.deleteRoom(id).subscribe(
          (response: RoomListItemModel[]) => {
            this.hotel.rooms = response;
            this.clearRoomBookingFormArray();
            this.createRoomBookingFormArray();
          },
          error => console.warn(error),
        );
      }
    })
  }

  updateRoom(id: number): void {
    this.router.navigate(['/admin/hotel/update-room/', id])
  }

  roomDetail(id: number): void {
    this.router.navigate(['/admin/hotel/room/', id])
  }

  backToHotelList() {
    this.router.navigate(['/hotel'])
  }

  makeBooking() {
    if (this.isLoggedIn == false) {
      this.notificationService.unsuccessful("Foglaláshoz jelentkezz be kérlek!");
      let loginDialogRef = this.dialog.open(LoginComponent, {
        height: '400px',
        width: '400px',
        data: {
          openedBy: "BookingButton"
        }
      });
      loginDialogRef.afterClosed().subscribe(
        response => {
          if (response) {
            this.loginService.authenticatedLoginDetailsModel.subscribe(
              (account) => {
               if (account && account.role === "ROLE_USER") {
                  this.account = account;
                  this.isLoggedIn = true;
                }
              }
            )
          }
        }
      )
    } else {
      let dialogRef = this.dialog.open(BookingFormDialogComponent, {
        height: '600px',
        width: '850px',
        data: this.createBookingFormDialogData(),
      });
      dialogRef.afterClosed().subscribe(
        response => {
          if (response) {
            this.router.navigate(['/hotel']);
          }
        }
      );
    }
  }

  createBookingFormDialogData() {
    const input = {...this.bookingForm.value};
    let roomReservationList = this.createRoomReservationList(input);
    return {
      hotel: this.createHotelDataToSend(),
      numberOfGuests: input.numberOfGuests,
      firstStartDate: this.getFirstStartDate(roomReservationList),
      lastEndDate: this.getLastEndDate(roomReservationList),
      roomReservationList: roomReservationList,
    }
  }

  createHotelDataToSend() {
    return {
      name: this.hotel.name,
      hotelType: this.hotel.hotelType,
      postalCode: this.hotel.postalCode,
      city: this.hotel.city,
      streetAddress: this.hotel.streetAddress,
    }
  }

  createRoomReservationList(input) {
    let roomReservationList = [];
    const startDate: Date = input.bookingDateRange.begin;
    const endDate: Date = input.bookingDateRange.end;
    const numberOfNights = Math.round((endDate.getTime() - startDate.getTime()) / 86400000);
    this.bookingForm.value.roomIdList.forEach((value, index) => {
        if (value) {
          const room: RoomShortListItemModel = {
            id: this.hotel.rooms[index].id,
            roomName: this.hotel.rooms[index].roomName,
            roomType: this.hotel.rooms[index].roomType,
            numberOfBeds: this.hotel.rooms[index].numberOfBeds,
            pricePerNight: this.hotel.rooms[index].pricePerNight,
          }
          const roomReservation: RoomReservationDetailsModel = {
            startDate, endDate, numberOfNights, room
          };
          roomReservationList.push(roomReservation)
        }
      }
    );
    return roomReservationList;
  }

  private getFirstStartDate(preRoomReservationList) {
    let firstStartDate = preRoomReservationList[0].startDate;
    preRoomReservationList.forEach(preRoomReservation => {
      if (preRoomReservation.startDate.getTime() < firstStartDate.getTime()) {
        firstStartDate = preRoomReservation.startDate;
      }
    });
    return firstStartDate;
  }

  private getLastEndDate(preRoomReservationList) {
    let lastEndDate = preRoomReservationList[0].endDate;
    preRoomReservationList.forEach(preRoomReservation => {
      if (preRoomReservation.endDate.getTime() > lastEndDate.getTime()) {
        lastEndDate = preRoomReservation.endDate;
      }
    });
    return lastEndDate;
  }

  private createRoomBookingFormArray() {
    this.hotel.rooms.forEach(() => {
        const control = new FormControl(false);
        (this.bookingForm.controls.roomIdList as FormArray).push(control);
      }
    );
  }

  private clearRoomBookingFormArray() {
    while ((this.bookingForm.controls.roomIdList as FormArray).length !== 0) {
      (this.bookingForm.controls.roomIdList as FormArray).removeAt(0)
    }
  }

  private createRoomFeaturesCheckboxControl() {
    this.roomFeatureTypeOption.forEach(() => {
        const control = new FormControl(false);
        (this.filterForm.controls.roomFeatures as FormArray).push(control);
      }
    );
  }

  private createRoomFeaturesFilterArrayToSend(): string[] {
    return this.filterForm.value.roomFeatures
      .map((roomFeatures, index) => roomFeatures ? this.roomFeatureTypeOption[index].name : null)
      .filter(roomFeatures => roomFeatures !== null);
  }

  getPublicId(imgURL: string) {
    return getPublicId(imgURL);
  }

  checkBoxValidator(array: FormArray): { required: boolean } {
    let counter = 0;
    array.getRawValue().forEach(value => value ? counter++ : counter);
    return counter < 1 ? {required: true} : null;
  }
}
