import dayjs from 'dayjs/esm';

export interface IFleetTrip {
  id: number;
  vehicleId?: number | null;
  driverId?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  distanceKm?: number | null;
  startLocation?: string | null;
  endLocation?: string | null;
  loadType?: string | null;
  loadDescription?: string | null;
  routeGeoCoordinates?: string | null;
  tripCost?: number | null;
}

export type NewFleetTrip = Omit<IFleetTrip, 'id'> & { id: null };
