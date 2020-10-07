import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NavbarComponent} from './components/navbar/navbar.component';
import {RoomFormComponent} from './components/room-form/room-form.component';
import {HotelListComponent} from "./components/hotel-list/hotel-list.component";
import {HotelFormComponent} from "./components/hotel-form/hotel-form.component";
import {HotelDetailsComponent} from "./components/hotel-detail/hotel-details.component";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {RoomDetailsComponent} from './components/room-details/room-details.component';
import {RegistrationComponent} from './components/account/registration/registration.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {PopupComponent} from './popup/popup.component';
import {MatIconModule} from "@angular/material/icon";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MaterialModule} from "./material.module";
import {MDBBootstrapModule} from "angular-bootstrap-md";
import {Cloudinary} from 'cloudinary-core/cloudinary-core-shrinkwrap';
import {CloudinaryModule} from "@cloudinary/angular-5.x";
import {BookingFormDialogComponent} from "./components/booking-form-dialog/booking-form-dialog.component";
import {LoginComponent} from "./components/account/login/login.component";
import {HttpRequestInterceptor} from "./utils/httpRequestInterceptor";
import {FileUploadModule} from "ng2-file-upload";
import {HotelImageManagerComponent} from './components/hotel-form/hotel-image-manager/hotel-image-manager.component';
import {ConfirmationComponent} from "./components/account/confirmation/confirmation.component";
import {AccountDetailsComponent} from './components/account/account-details/account-details.component';
import {ActualRoomBookingsComponent} from "./components/room-details/actual-room-bookings/actual-room-bookings.component";
import {PastRoomBookingsComponent} from "./components/room-details/past-room-bookings/past-room-bookings.component";
import {BookingDetailDialogComponent} from "./components/booking-detail-dialog/booking-detail-dialog.component";
import {HotelBookingsComponent} from "./components/hotel-bookings/hotel-bookings.component";
import {UserBookingsComponent} from "./components/user-bookings/user-bookings.component";
import {ActualUserBookingsComponent} from "./components/user-bookings/actual-user-bookings/actual-user-bookings.component";
import {PastUserBookingsComponent} from "./components/user-bookings/past-user-bookings/past-user-bookings.component";
import {ActualHotelBookingsComponent} from "./components/hotel-bookings/actual-hotel-bookings/actual-hotel-bookings.component";
import {FutureHotelBookingsComponent} from "./components/hotel-bookings/future-hotel-bookings/future-hotel-bookings.component";
import {PastHotelBookingsComponent} from "./components/hotel-bookings/past-hotel-bookings/past-hotel-bookings.component";
import {AccountEditComponent} from './components/account/account-edit/account-edit.component';
import {HomeComponent} from "./components/home/home.component";
import {FooterComponent} from "./components/footer/footer.component";
import {HotelBookingsCalendarComponent} from "./components/hotel-bookings/hotel-bookings-calendar/hotel-bookings-calendar.component";
import {HotelBookingsStatisticsComponent} from "./components/hotel-bookings/hotel-bookings-statistics/hotel-bookings-statistics.component";
import {NotfoundComponent} from "./components/notfound/notfound.component";
import {PaginationComponent} from "./components/pagination/pagination.component";
import {DemoWarningDialogComponent} from "./components/demo-warning-dialog/demo-warning-dialog.component";
import {AgmCoreModule} from "@agm/core";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  SatDatepickerModule,
  SatNativeDateModule
} from "saturn-datepicker";
import {APP_DATE_FORMATS, MyDateAdapter} from "./utils/myDateAdapter";
import {AgmOverlays} from "agm-overlays";
import {environment} from "../environments/environment";

export const cloudinary = {
  Cloudinary: Cloudinary
};

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    RoomFormComponent,
    RoomDetailsComponent,
    ActualRoomBookingsComponent,
    PastRoomBookingsComponent,
    HotelFormComponent,
    HotelListComponent,
    HotelDetailsComponent,
    HotelBookingsComponent,
    ActualHotelBookingsComponent,
    FutureHotelBookingsComponent,
    PastHotelBookingsComponent,
    HotelBookingsCalendarComponent,
    HotelBookingsStatisticsComponent,
    RegistrationComponent,
    PopupComponent,
    BookingFormDialogComponent,
    BookingDetailDialogComponent,
    LoginComponent,
    HotelImageManagerComponent,
    LoginComponent,
    UserBookingsComponent,
    ActualUserBookingsComponent,
    PastUserBookingsComponent,
    ConfirmationComponent,
    AccountDetailsComponent,
    AccountEditComponent,
    HomeComponent,
    FooterComponent,
    NotfoundComponent,
    PaginationComponent,
    DemoWarningDialogComponent,

  ],
    imports: [
      HttpClientModule,
      ReactiveFormsModule,
      BrowserModule,
      AppRoutingModule,
      BrowserAnimationsModule,
      MaterialModule,
      MatIconModule,
      MatDialogModule,
      MDBBootstrapModule.forRoot(),
      CloudinaryModule.forRoot(cloudinary, {cloud_name: environment.CloudinaryModule_cloud_name}),
      FileUploadModule,
      AgmCoreModule.forRoot({apiKey: environment.AgmCoreModule_apiKey}),
      AgmOverlays,
      MatDatepickerModule,
      SatNativeDateModule,
      SatDatepickerModule,
    ],
  providers: [
    [{provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true}],
    [{provide: MatDialogRef}],
    [{provide: MAT_DIALOG_DATA, multi: true}],
    [{provide: DateAdapter, useClass: MyDateAdapter}],
    [{provide: MAT_DATE_FORMATS, useValue: APP_DATE_FORMATS}],

  ],
  bootstrap: [AppComponent],
  entryComponents: [
    PopupComponent,
    BookingFormDialogComponent,
    BookingDetailDialogComponent,
    LoginComponent,
    RegistrationComponent,
    DemoWarningDialogComponent
  ]
})
export class AppModule {
}
