import dayjs from 'dayjs/esm';

import { IBudget, NewBudget } from './budget.model';

export const sampleWithRequiredData: IBudget = {
  id: 28039,
  name: 'exaggerate unlike',
  startDate: dayjs('2025-10-08T12:25'),
  endDate: dayjs('2025-10-09T01:11'),
  amount: 32157.28,
};

export const sampleWithPartialData: IBudget = {
  id: 13711,
  name: 'unrealistic',
  startDate: dayjs('2025-10-08T16:52'),
  endDate: dayjs('2025-10-08T22:28'),
  amount: 25735.97,
};

export const sampleWithFullData: IBudget = {
  id: 6840,
  name: 'repeatedly',
  startDate: dayjs('2025-10-09T03:07'),
  endDate: dayjs('2025-10-08T09:21'),
  accountId: 29994,
  amount: 22665.11,
};

export const sampleWithNewData: NewBudget = {
  name: 'separately',
  startDate: dayjs('2025-10-09T03:26'),
  endDate: dayjs('2025-10-08T20:35'),
  amount: 7944.32,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
