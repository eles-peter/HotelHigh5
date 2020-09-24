export interface HotelCreateItemModel {
	id?: number;
	name: string;
	postalCode: string;
	city: string;
	streetAddress: string;
	hotelType: string;
	description: string;
	hotelFeatures: string[];
}
