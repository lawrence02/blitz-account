import dayjs from 'dayjs/esm';
import { IncidentType } from 'app/entities/enumerations/incident-type.model';

export interface IIncidentLog {
  id: number;
  vehicleId?: number | null;
  tripId?: number | null;
  incidentDate?: dayjs.Dayjs | null;
  type?: keyof typeof IncidentType | null;
  description?: string | null;
  cost?: number | null;
}

export type NewIncidentLog = Omit<IIncidentLog, 'id'> & { id: null };
