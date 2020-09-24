import {RoomFeatureTypeOptionModel} from "./roomFeatureTypeOption.model";

export interface RoomFilterDataModel {
  startDate: Date;
  endDate: Date;
  roomFeatures?: RoomFeatureTypeOptionModel[];
}
