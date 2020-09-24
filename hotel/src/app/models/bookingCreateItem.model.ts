import {RoomReservationShortItemModel} from "./roomReservationShortItem.model";

export interface BookingCreateItemModel {
  guestAccountName?: string;
  firstName?: string;
  lastName?: string;
  address?: string;
  email?: string;
  remark: string;
  numberOfGuests: number;
  roomReservationList: RoomReservationShortItemModel[];
}
