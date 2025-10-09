import { IChartOfAccount, NewChartOfAccount } from './chart-of-account.model';

export const sampleWithRequiredData: IChartOfAccount = {
  id: 22758,
  name: 'and among yuck',
  accountType: 'ASSET',
  code: 'shy since',
};

export const sampleWithPartialData: IChartOfAccount = {
  id: 32007,
  name: 'ice-cream accept',
  accountType: 'INCOME',
  code: 'ew joyous',
  currentBalance: 23115.35,
};

export const sampleWithFullData: IChartOfAccount = {
  id: 7717,
  name: 'phew disappointment',
  accountType: 'EXPENSE',
  code: 'although',
  initialBalance: 13759.42,
  currentBalance: 27932.23,
};

export const sampleWithNewData: NewChartOfAccount = {
  name: 'hotfoot',
  accountType: 'ASSET',
  code: 'fooey for',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
