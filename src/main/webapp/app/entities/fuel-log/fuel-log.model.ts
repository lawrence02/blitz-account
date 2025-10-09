import dayjs from 'dayjs/esm';

export interface IFuelLog {
  id: number;
  vehicleId?: number | null;
  date?: dayjs.Dayjs | null;
  fuelVolume?: number | null;
  fuelCost?: number | null;
  location?: string | null;
  tripId?: number | null;
}

export type NewFuelLog = Omit<IFuelLog, 'id'> & { id: null };
