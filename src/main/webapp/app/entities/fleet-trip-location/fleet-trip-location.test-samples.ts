import dayjs from 'dayjs/esm';

import { IFleetTripLocation, NewFleetTripLocation } from './fleet-trip-location.model';

export const sampleWithRequiredData: IFleetTripLocation = {
  id: 7566,
  fleetTripId: 12368,
  timestamp: dayjs('2025-10-08T12:59'),
  latitude: 7880.4,
  longitude: 10227.25,
};

export const sampleWithPartialData: IFleetTripLocation = {
  id: 17107,
  fleetTripId: 17252,
  timestamp: dayjs('2025-10-08T08:50'),
  latitude: 7007.89,
  longitude: 10226.1,
  speed: 26111.78,
  heading: 9156.77,
};

export const sampleWithFullData: IFleetTripLocation = {
  id: 32735,
  fleetTripId: 4379,
  timestamp: dayjs('2025-10-08T08:56'),
  latitude: 13112.2,
  longitude: 9223,
  speed: 10400.09,
  heading: 1691.7,
};

export const sampleWithNewData: NewFleetTripLocation = {
  fleetTripId: 1959,
  timestamp: dayjs('2025-10-09T08:38'),
  latitude: 19719.87,
  longitude: 2879.77,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
