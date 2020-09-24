export interface RoomDetailsModel {
	id: number;
	roomName: string;
	roomType: string;
	numberOfBeds: number;
	roomArea: number;
	hotelId: number;
  hotelName: string;
	pricePerNight: number;
	roomImageUrl: string;
	description: string;
	roomFeatures: string[];
}
