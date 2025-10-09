import dayjs from 'dayjs/esm';

import { IRecurringTransaction, NewRecurringTransaction } from './recurring-transaction.model';

export const sampleWithRequiredData: IRecurringTransaction = {
  id: 16467,
  name: 'geez among',
  amount: 14644.65,
  frequency: 'apologise drat',
  startDate: dayjs('2025-10-08T14:46'),
};

export const sampleWithPartialData: IRecurringTransaction = {
  id: 24422,
  name: 'unfinished',
  amount: 1455.52,
  frequency: 'because',
  startDate: dayjs('2025-10-09T08:25'),
  accountId: 11516,
  vatRateId: 30517,
};

export const sampleWithFullData: IRecurringTransaction = {
  id: 5424,
  name: 'now',
  amount: 16373.65,
  frequency: 'too',
  startDate: dayjs('2025-10-09T07:44'),
  endDate: dayjs('2025-10-08T11:02'),
  accountId: 17683,
  currencyId: 7620,
  vatRateId: 32751,
};

export const sampleWithNewData: NewRecurringTransaction = {
  name: 'retool hastily violently',
  amount: 27377.89,
  frequency: 'onto elevator',
  startDate: dayjs('2025-10-08T11:38'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
