export interface HotelListItemModel {
	id: number;
	name: string;
	postalCode: string;
	city: string;
	streetAddress: string;
  longitude: number;
  latitude: number;
	hotelType: string;
	hotelImageUrl: string;
  shortDescription: string;
  bestPricePerNightPerPerson: number;
  avgRate: number;
}
