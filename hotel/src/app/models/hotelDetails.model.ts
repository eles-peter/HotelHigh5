import {RoomListItemModel} from "./roomListItem.model";

export interface HotelDetailsModel {
	id: number;
	name: string;
	postalCode: string;
	city: string;
	streetAddress: string;
	hotelType: string;
	hotelCapacity: number;
  rooms: RoomListItemModel[]
	hotelImageUrl: Array<string>;
	description: string;
	hotelFeatures: string[];
  avgRate: number;
}
