import { IBankAccount, NewBankAccount } from './bank-account.model';

export const sampleWithRequiredData: IBankAccount = {
  id: 17167,
  name: 'um via',
  accountNumber: 'by',
};

export const sampleWithPartialData: IBankAccount = {
  id: 17763,
  name: 'term pfft',
  accountNumber: 'delightfully',
};

export const sampleWithFullData: IBankAccount = {
  id: 13483,
  name: 'gigantic',
  accountNumber: 'fairly fooey ugh',
  bankName: 'twine',
};

export const sampleWithNewData: NewBankAccount = {
  name: 'clonk porter',
  accountNumber: 'phew vice near',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
