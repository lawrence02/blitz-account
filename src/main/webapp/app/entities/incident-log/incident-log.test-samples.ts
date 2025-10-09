import dayjs from 'dayjs/esm';

import { IIncidentLog, NewIncidentLog } from './incident-log.model';

export const sampleWithRequiredData: IIncidentLog = {
  id: 23989,
  vehicleId: 26166,
  incidentDate: dayjs('2025-10-08T17:04'),
};

export const sampleWithPartialData: IIncidentLog = {
  id: 311,
  vehicleId: 4266,
  incidentDate: dayjs('2025-10-09T03:22'),
  cost: 10067.59,
};

export const sampleWithFullData: IIncidentLog = {
  id: 28934,
  vehicleId: 11434,
  tripId: 3488,
  incidentDate: dayjs('2025-10-08T12:20'),
  type: 'BREAKDOWN',
  description: 'indeed',
  cost: 5001.89,
};

export const sampleWithNewData: NewIncidentLog = {
  vehicleId: 22317,
  incidentDate: dayjs('2025-10-09T01:27'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
