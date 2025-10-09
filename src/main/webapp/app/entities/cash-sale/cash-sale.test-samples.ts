import dayjs from 'dayjs/esm';

import { ICashSale, NewCashSale } from './cash-sale.model';

export const sampleWithRequiredData: ICashSale = {
  id: 24145,
};

export const sampleWithPartialData: ICashSale = {
  id: 8966,
  unitPrice: 5678.67,
  vatRateId: 14630,
  saleDate: dayjs('2025-10-08T18:56'),
};

export const sampleWithFullData: ICashSale = {
  id: 7034,
  productId: 10314,
  quantity: 1993,
  unitPrice: 22887.21,
  vatRateId: 7448,
  currencyId: 23159,
  saleDate: dayjs('2025-10-09T06:47'),
};

export const sampleWithNewData: NewCashSale = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
