import { ICurrency, NewCurrency } from './currency.model';

export const sampleWithRequiredData: ICurrency = {
  id: 28187,
  code: 'including',
  name: 'knottily',
};

export const sampleWithPartialData: ICurrency = {
  id: 29243,
  code: 'as spice',
  name: 'huzzah',
  symbol: 'following relative',
};

export const sampleWithFullData: ICurrency = {
  id: 30512,
  code: 'serene',
  name: 'far enfold',
  symbol: 'plumber yahoo ack',
};

export const sampleWithNewData: NewCurrency = {
  code: 'drat per avalanche',
  name: 'whose absent ambitious',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
