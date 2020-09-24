import {BookingListItemForHotelModel} from "./bookingListItemForHotel.model";

export interface BookingSublistForHotelModel {
  bookingSubList: Array<BookingListItemForHotelModel>;
  listPageNumber: number;
  fullNumberOfPages: number;
}
