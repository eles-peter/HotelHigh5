export interface RoomReservationDataModel {
  roomReservationId: number;
  bookingId: number;
  guestFirstName: string;
  guestLastName: string;
  startDate: Date;
  endDate: Date;
  numberOfGuests: number;
}
