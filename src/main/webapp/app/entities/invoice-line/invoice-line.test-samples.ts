import { IInvoiceLine, NewInvoiceLine } from './invoice-line.model';

export const sampleWithRequiredData: IInvoiceLine = {
  id: 4931,
  invoiceId: 15085,
};

export const sampleWithPartialData: IInvoiceLine = {
  id: 21175,
  invoiceId: 29124,
  vatRateId: 25304,
};

export const sampleWithFullData: IInvoiceLine = {
  id: 23631,
  invoiceId: 32691,
  productId: 10083,
  quantity: 16931,
  unitPrice: 2471.63,
  vatRateId: 4933,
};

export const sampleWithNewData: NewInvoiceLine = {
  invoiceId: 26337,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
