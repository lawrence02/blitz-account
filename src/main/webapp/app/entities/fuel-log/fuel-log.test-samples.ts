import dayjs from 'dayjs/esm';

import { IFuelLog, NewFuelLog } from './fuel-log.model';

export const sampleWithRequiredData: IFuelLog = {
  id: 24261,
  vehicleId: 22649,
  date: dayjs('2025-10-08T13:27'),
};

export const sampleWithPartialData: IFuelLog = {
  id: 27336,
  vehicleId: 2224,
  date: dayjs('2025-10-09T02:58'),
  fuelCost: 29014.11,
  location: 'pliers than which',
  tripId: 22945,
};

export const sampleWithFullData: IFuelLog = {
  id: 7210,
  vehicleId: 4771,
  date: dayjs('2025-10-08T21:58'),
  fuelVolume: 8179.94,
  fuelCost: 23281.39,
  location: 'lashes zowie',
  tripId: 5520,
};

export const sampleWithNewData: NewFuelLog = {
  vehicleId: 13359,
  date: dayjs('2025-10-08T19:15'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
