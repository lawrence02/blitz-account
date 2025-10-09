import dayjs from 'dayjs/esm';

import { IBankTransaction, NewBankTransaction } from './bank-transaction.model';

export const sampleWithRequiredData: IBankTransaction = {
  id: 29331,
  bankAccountId: 19068,
  transactionDate: dayjs('2025-10-08T14:08'),
  amount: 31716.95,
  direction: 'CREDIT',
};

export const sampleWithPartialData: IBankTransaction = {
  id: 24229,
  bankAccountId: 14865,
  transactionDate: dayjs('2025-10-08T11:24'),
  amount: 2436.39,
  direction: 'CREDIT',
  description: 'below',
};

export const sampleWithFullData: IBankTransaction = {
  id: 3029,
  bankAccountId: 8968,
  transactionDate: dayjs('2025-10-08T13:15'),
  reference: 'bend',
  amount: 8853.66,
  direction: 'CREDIT',
  relatedPaymentId: 9414,
  description: 'woot',
};

export const sampleWithNewData: NewBankTransaction = {
  bankAccountId: 31995,
  transactionDate: dayjs('2025-10-09T07:39'),
  amount: 31384.32,
  direction: 'DEBIT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
