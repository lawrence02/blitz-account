import dayjs from 'dayjs/esm';

import { IQuote, NewQuote } from './quote.model';

export const sampleWithRequiredData: IQuote = {
  id: 24402,
  clientName: 'questionably vacantly capsize',
  issueDate: dayjs('2025-10-08T16:44'),
  status: 'DRAFT',
};

export const sampleWithPartialData: IQuote = {
  id: 18049,
  clientName: 'thorny pish except',
  issueDate: dayjs('2025-10-08T20:21'),
  status: 'DRAFT',
  totalAmount: 15625.71,
};

export const sampleWithFullData: IQuote = {
  id: 28771,
  clientName: 'phew awkwardly',
  issueDate: dayjs('2025-10-08T19:30'),
  status: 'DRAFT',
  currencyId: 24143,
  vatRateId: 1680,
  totalAmount: 5827.46,
};

export const sampleWithNewData: NewQuote = {
  clientName: 'huzzah',
  issueDate: dayjs('2025-10-08T11:51'),
  status: 'DRAFT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
