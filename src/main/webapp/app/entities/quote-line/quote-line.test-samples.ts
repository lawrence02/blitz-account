import { IQuoteLine, NewQuoteLine } from './quote-line.model';

export const sampleWithRequiredData: IQuoteLine = {
  id: 11026,
  quoteId: 11088,
};

export const sampleWithPartialData: IQuoteLine = {
  id: 19001,
  quoteId: 19594,
  quantity: 18906,
  vatRateId: 798,
};

export const sampleWithFullData: IQuoteLine = {
  id: 22540,
  quoteId: 1434,
  productId: 27673,
  quantity: 9968,
  unitPrice: 18505.92,
  vatRateId: 23126,
};

export const sampleWithNewData: NewQuoteLine = {
  quoteId: 4237,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
