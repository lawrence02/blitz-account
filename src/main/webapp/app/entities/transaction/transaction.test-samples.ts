import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: 3793,
  type: 'EXPENSE',
  amount: 2419.29,
  transactionDate: dayjs('2025-10-09T01:48'),
};

export const sampleWithPartialData: ITransaction = {
  id: 1969,
  type: 'INCOME',
  amount: 26479.76,
  transactionDate: dayjs('2025-10-08T23:20'),
  vatRateId: 32375,
};

export const sampleWithFullData: ITransaction = {
  id: 20285,
  type: 'INCOME',
  amount: 25161.85,
  transactionDate: dayjs('2025-10-08T23:26'),
  accountId: 22521,
  vatRateId: 21574,
  currencyId: 20455,
};

export const sampleWithNewData: NewTransaction = {
  type: 'EXPENSE',
  amount: 29048,
  transactionDate: dayjs('2025-10-09T07:48'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
