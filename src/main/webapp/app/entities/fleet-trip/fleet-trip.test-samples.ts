import dayjs from 'dayjs/esm';

import { IFleetTrip, NewFleetTrip } from './fleet-trip.model';

export const sampleWithRequiredData: IFleetTrip = {
  id: 803,
  vehicleId: 12509,
  startDate: dayjs('2025-10-08T12:18'),
};

export const sampleWithPartialData: IFleetTrip = {
  id: 23945,
  vehicleId: 24850,
  startDate: dayjs('2025-10-08T11:01'),
  endDate: dayjs('2025-10-08T18:46'),
  startLocation: 'juvenile object stool',
  endLocation: 'unlucky',
  loadType: 'regarding blowgun',
};

export const sampleWithFullData: IFleetTrip = {
  id: 22312,
  vehicleId: 18292,
  driverId: 5278,
  startDate: dayjs('2025-10-08T10:11'),
  endDate: dayjs('2025-10-09T07:54'),
  distanceKm: 18353.15,
  startLocation: 'triumphantly hm antagonize',
  endLocation: 'ha',
  loadType: 'upside-down yum before',
  loadDescription: 'trick zowie abnormally',
  routeGeoCoordinates: '../fake-data/blob/hipster.txt',
  tripCost: 14030.88,
};

export const sampleWithNewData: NewFleetTrip = {
  vehicleId: 18567,
  startDate: dayjs('2025-10-08T13:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
