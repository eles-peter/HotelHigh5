import {RoomShortListItemModel} from "./roomShortListItem.model";
import {AccountListItemModel} from "./accountListItem.model";

export interface BookingListItemForHotelModel {
  id: number;
  guest: AccountListItemModel;
  startDate: Date;
  endDate: Date;
  reservedRooms: RoomShortListItemModel[];
  numberOfGuests: number;
  dateOfBooking: Date;
  priceOfBooking: number;
}
