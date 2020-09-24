export interface RoomCreateItemModel {
  id?: number;
  roomName: string;
  roomType: string;
  numberOfBeds: number;
  roomArea: number;
  pricePerNight: number;
  roomImageUrl: string;
  description: string;
  roomFeatures: string[];
  hotelId: number;
}
