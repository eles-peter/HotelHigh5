import {HotelFeatureTypeOptionModel} from "./hotelFeatureTypeOption.model";
import {HotelTypeOptionModel} from "./hotelTypeOption.model";

export interface HotelFormDataModel {
	hotelType: HotelTypeOptionModel[];
	hotelFeatures: HotelFeatureTypeOptionModel[];
}
