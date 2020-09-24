import {HotelFeatureTypeOptionModel} from "./hotelFeatureTypeOption.model";

export interface HotelFilterDataModel {
  numberOfGuests: number;
  startDate: Date;
  endDate: Date;
  hotelFeatures?: HotelFeatureTypeOptionModel[];
}
