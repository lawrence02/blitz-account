import dayjs from 'dayjs/esm';

export interface IFleetTripLocation {
  id: number;
  fleetTripId?: number | null;
  timestamp?: dayjs.Dayjs | null;
  latitude?: number | null;
  longitude?: number | null;
  speed?: number | null;
  heading?: number | null;
}

export type NewFleetTripLocation = Omit<IFleetTripLocation, 'id'> & { id: null };
