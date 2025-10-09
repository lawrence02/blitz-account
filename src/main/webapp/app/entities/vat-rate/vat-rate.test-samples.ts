import { IVATRate, NewVATRate } from './vat-rate.model';

export const sampleWithRequiredData: IVATRate = {
  id: 14050,
  name: 'aha alongside',
  percentage: 8147.08,
};

export const sampleWithPartialData: IVATRate = {
  id: 9789,
  name: 'supposing',
  percentage: 26755.24,
};

export const sampleWithFullData: IVATRate = {
  id: 6410,
  name: 'given abaft',
  percentage: 16276.06,
};

export const sampleWithNewData: NewVATRate = {
  name: 'qua',
  percentage: 24199.1,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
