import {RoomFeatureTypeOptionModel} from "./roomFeatureTypeOption.model";
import {RoomTypeOptionModel} from "./roomTypeOption.model";

export interface RoomFormDataModel {
	roomType: RoomTypeOptionModel[];
	roomFeatures: RoomFeatureTypeOptionModel[];
}
