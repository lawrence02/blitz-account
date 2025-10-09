import dayjs from 'dayjs/esm';

export interface IServiceLog {
  id: number;
  vehicleId?: number | null;
  serviceDate?: dayjs.Dayjs | null;
  description?: string | null;
  cost?: number | null;
  mileageAtService?: number | null;
  supplierId?: number | null;
}

export type NewServiceLog = Omit<IServiceLog, 'id'> & { id: null };
