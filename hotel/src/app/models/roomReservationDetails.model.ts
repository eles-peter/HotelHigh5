import {RoomShortListItemModel} from "./roomShortListItem.model";

export interface RoomReservationDetailsModel {
  id?: number;
  startDate: Date;
  endDate: Date;
  numberOfNights: number;
  room: RoomShortListItemModel;
}
