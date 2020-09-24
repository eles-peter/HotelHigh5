import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {HotelListItemModel} from "../models/hotelListItem.model";
import {HotelCreateItemModel} from "../models/hotelCreateItem.model";
import {HotelFormDataModel} from "../models/hotelFormData.model";
import {HotelDetailsModel} from "../models/hotelDetails.model";
import {environment} from "../../environments/environment";
import {HotelListItemSubListModel} from "../models/hotelListItemSubList.model";
import {HotelItemForHomePageModel} from "../models/hotelItemForHomePage.model";
import {HotelShortItemModel} from "../models/hotelShortItem.model";

const BASE_URL = environment.BASE_URL + '/api/hotel';

@Injectable({
  providedIn: 'root'
})
export class HotelService {

  constructor(private http: HttpClient) {
  }

  listHotel(listPageNumber?: number): Observable<HotelListItemSubListModel> {
    let params = new HttpParams();
    if (listPageNumber != null) {
      params = params.set('listPageNumber', String(listPageNumber));
    } else {
      params = params.set('listPageNumber', '0');
    }
    return this.http.get<HotelListItemSubListModel>(BASE_URL, {params});
  }

  getFilteredHotelList(filterData: { numberOfGuests: string; startDate: string; endDate: string; hotelFeatures?: string }, listPageNumber?: number):
    Observable<HotelListItemSubListModel> {
    let params = new HttpParams()
      .set('numberOfGuests', filterData.numberOfGuests)
      .set('startDate', filterData.startDate)
      .set('endDate', filterData.endDate)
      .set('hotelFeatures', filterData.hotelFeatures);
    if (listPageNumber != null) {
      params = params.set('listPageNumber', String(listPageNumber));
    } else {
      params = params.set('listPageNumber', '0');
    }
    return this.http.get<HotelListItemSubListModel>(BASE_URL + '/filter', {params});
  }

  getTheBestHotelListPriceForHomePage(): Observable<Array<HotelItemForHomePageModel>> {
    return this.http.get<Array<HotelItemForHomePageModel>>(BASE_URL + '/bestprice');
  }

  getTheBestAvgRateHotelListForHomePage(): Observable<Array<HotelItemForHomePageModel>> {
    return this.http.get<Array<HotelItemForHomePageModel>>(BASE_URL + '/bestavgrate');
  }

  getRandomHotelListForHomePage(): Observable<Array<HotelItemForHomePageModel>> {
    return this.http.get<Array<HotelItemForHomePageModel>>(BASE_URL + '/random');
  }

  deleteHotel(id: number): Observable<Array<HotelListItemModel>> {
    return this.http.delete<Array<HotelListItemModel>>(BASE_URL + '/' + id);
  }

  createHotel(data: HotelCreateItemModel): Observable<number> {
    return this.http.post<number>(BASE_URL, data);
  }

  getHotelFormData(): Observable<HotelFormDataModel> {
    return this.http.get<HotelFormDataModel>(BASE_URL + '/formData');
  }

  updateHotel(data: HotelCreateItemModel, id: number): Observable<any> {
    data.id = id;
    return this.http.put(BASE_URL + '/' + id, data);
  }

  hotelForUpdate(id: string): Observable<HotelCreateItemModel> {
    return this.http.get<HotelCreateItemModel>(BASE_URL + '/formData/' + id);
  }

  hotelDetail(id: string): Observable<HotelDetailsModel> {
    return this.http.get<HotelDetailsModel>(BASE_URL + '/' + id);
  }

  filteredHotelDetail(id: string, filterData: { numberOfGuests: string; startDate: string; endDate: string }) {
    let params = new HttpParams()
      .set('numberOfGuests', filterData.numberOfGuests)
      .set('startDate', filterData.startDate)
      .set('endDate', filterData.endDate);
    return this.http.get<HotelDetailsModel>(BASE_URL + '/' + id + '/filter', {params});
  }

  getHotelShortItem(id: number): Observable<HotelShortItemModel> {
    return this.http.get<HotelShortItemModel>(BASE_URL + '/short/' + id);
  }
  uploadImage(formData: FormData, id: number): Observable<string> {
    return this.http.post<string>(BASE_URL + '/uploadImage/' + id, formData);
  }

  deleteImage(image: string, hotelIdFromLogin: number) {
    const formData = new FormData();
    formData.append('imageURL', image);
    return this.http.post(BASE_URL + "/deleteImage/" + hotelIdFromLogin, formData);
  }

  getHotelImages(hotelId: number): Observable<Array<string>> {
    return this.http.get<Array<string>>(BASE_URL + "/images/" + hotelId);
  }
}
