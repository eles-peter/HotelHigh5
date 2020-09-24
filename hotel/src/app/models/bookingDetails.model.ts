import {HotelShortItemModel} from "./hotelShortItem.model";
import {AccountDetailsModel} from "./accountDetails.model";
import {RoomReservationDetailsModel} from "./roomReservationDetails.model";

export interface BookingDetailsModel {
  id: number;
  hotel: HotelShortItemModel;
  guest: AccountDetailsModel;
  remark: string;
  roomReservationList: RoomReservationDetailsModel[];
  numberOfGuests: number;
  dateOfBooking: Date;
  priceOfBooking: number;
}

