import dayjs from 'dayjs/esm';

import { IExchangeRate, NewExchangeRate } from './exchange-rate.model';

export const sampleWithRequiredData: IExchangeRate = {
  id: 9191,
  baseCurrencyId: 5979,
  targetCurrencyId: 22659,
  rate: 10840.5,
  rateDate: dayjs('2025-10-08T17:00'),
};

export const sampleWithPartialData: IExchangeRate = {
  id: 200,
  baseCurrencyId: 26249,
  targetCurrencyId: 9741,
  rate: 9322.47,
  rateDate: dayjs('2025-10-08T12:08'),
};

export const sampleWithFullData: IExchangeRate = {
  id: 6523,
  baseCurrencyId: 10762,
  targetCurrencyId: 27300,
  rate: 15369.92,
  rateDate: dayjs('2025-10-08T20:35'),
};

export const sampleWithNewData: NewExchangeRate = {
  baseCurrencyId: 30251,
  targetCurrencyId: 25000,
  rate: 8240.86,
  rateDate: dayjs('2025-10-08T10:07'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
