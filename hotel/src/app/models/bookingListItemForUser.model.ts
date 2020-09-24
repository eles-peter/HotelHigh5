import {RoomShortListItemModel} from "./roomShortListItem.model";
import {HotelShortItemModel} from "./hotelShortItem.model";

export interface BookingListItemForUserModel {
  id: number;
  guestName: string;
  startDate: Date;
  endDate: Date;
  hotel: HotelShortItemModel
  reservedRooms: RoomShortListItemModel[];
  numberOfGuests: number;
  dateOfBooking: Date;
  priceOfBooking: number;
}
