import dayjs from 'dayjs/esm';

import { IServiceLog, NewServiceLog } from './service-log.model';

export const sampleWithRequiredData: IServiceLog = {
  id: 4676,
  vehicleId: 11388,
  serviceDate: dayjs('2025-10-08T22:12'),
};

export const sampleWithPartialData: IServiceLog = {
  id: 29093,
  vehicleId: 27772,
  serviceDate: dayjs('2025-10-08T20:51'),
  mileageAtService: 7153.82,
};

export const sampleWithFullData: IServiceLog = {
  id: 26187,
  vehicleId: 30942,
  serviceDate: dayjs('2025-10-09T05:40'),
  description: 'ugh gah above',
  cost: 24093,
  mileageAtService: 15576.81,
  supplierId: 22121,
};

export const sampleWithNewData: NewServiceLog = {
  vehicleId: 19502,
  serviceDate: dayjs('2025-10-08T15:30'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
