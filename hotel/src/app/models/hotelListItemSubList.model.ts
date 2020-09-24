import {HotelListItemModel} from "./hotelListItem.model";

export interface HotelListItemSubListModel {
  hotelSubList: HotelListItemModel[];
  listPageNumber: number;
  fullNumberOfPages: number;
}
