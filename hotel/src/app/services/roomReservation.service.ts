import {environment} from "../../environments/environment";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {RoomReservationDataModel} from "../models/roomReservationData.model";
import {Observable} from "rxjs";
import {dateToJsonDateString} from "../utils/dateUtils";

const BASE_URL = environment.BASE_URL + '/api/roomReservation';

@Injectable({
  providedIn: 'root'
})
export class RoomReservationService {

  constructor(private http: HttpClient) {
  }

  updateRoomReservation(modifiedRoomReservationData: RoomReservationDataModel): Observable<any> {
    let roomReservationId = modifiedRoomReservationData.roomReservationId;
    const parsedDate = this.parseRoomReservationData(modifiedRoomReservationData);
    return this.http.put(BASE_URL + '/' + roomReservationId, parsedDate);
  }

  updateAllRoomReservationInBooking(modifiedRoomReservations: RoomReservationDataModel[]): Observable<any>  {
    let parseDataList = [];
    modifiedRoomReservations.forEach(modifiedRoomReservation => {
      let parseDate = this.parseRoomReservationData(modifiedRoomReservation);
      parseDataList.push(parseDate);
    });
    return this.http.put(BASE_URL, parseDataList);
  }


  deleteRoomReservation(roomReservationId: number): Observable<any> {
    return this.http.delete(BASE_URL + '/' + roomReservationId);
  }

  private parseRoomReservationData(data: RoomReservationDataModel) {
    return {
      roomReservationId: data.roomReservationId,
      bookingId: data.bookingId,
      guestFirstName: data.guestFirstName,
      guestLastName: data.guestLastName,
      startDate: dateToJsonDateString(data.startDate),
      endDate: dateToJsonDateString(data.endDate),
      numberOfGuests: data.numberOfGuests,
    }
  }
}
