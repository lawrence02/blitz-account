import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 4942,
  amount: 18523.62,
  paymentDate: dayjs('2025-10-08T18:29'),
  method: 'CASH',
};

export const sampleWithPartialData: IPayment = {
  id: 18677,
  amount: 11071.62,
  paymentDate: dayjs('2025-10-09T05:10'),
  method: 'MOBILE_MONEY',
  bankAccountId: 25009,
};

export const sampleWithFullData: IPayment = {
  id: 28239,
  amount: 11240.4,
  paymentDate: dayjs('2025-10-08T19:30'),
  method: 'BANK_TRANSFER',
  currencyId: 25638,
  bankAccountId: 23167,
  invoiceId: 10777,
};

export const sampleWithNewData: NewPayment = {
  amount: 7354.94,
  paymentDate: dayjs('2025-10-08T11:37'),
  method: 'CASH',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
