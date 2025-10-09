import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 29050,
  clientName: 'search why',
  issueDate: dayjs('2025-10-09T07:55'),
  totalAmount: 2181.18,
  paymentStatus: 'PAID',
};

export const sampleWithPartialData: IInvoice = {
  id: 20215,
  clientName: 'warmly',
  issueDate: dayjs('2025-10-08T19:49'),
  status: 'REJECTED',
  totalAmount: 31505.64,
  paymentStatus: 'UNPAID',
};

export const sampleWithFullData: IInvoice = {
  id: 19628,
  clientName: 'yesterday times',
  issueDate: dayjs('2025-10-08T16:44'),
  dueDate: dayjs('2025-10-08T13:38'),
  status: 'SENT',
  currencyId: 4194,
  vatRateId: 25940,
  totalAmount: 19960.19,
  paidAmount: 15614.5,
  paymentStatus: 'PARTIALLY_PAID',
};

export const sampleWithNewData: NewInvoice = {
  clientName: 'within about',
  issueDate: dayjs('2025-10-08T20:20'),
  totalAmount: 5947.02,
  paymentStatus: 'UNPAID',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
