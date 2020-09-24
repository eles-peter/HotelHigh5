import {RoomReservationDataModel} from "./roomReservationData.model";

export interface RoomBookingDataModel {
  roomId: number;
  roomName: string;
  roomType: string;
  numberOfBeds: number;
  pricePerNight: number;
  roomReservationDataList: RoomReservationDataModel[];
}
