import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 11737,
  name: 'hm sleepily',
};

export const sampleWithPartialData: IProduct = {
  id: 28292,
  name: 'delightfully mild',
  supplierId: 12157,
};

export const sampleWithFullData: IProduct = {
  id: 4403,
  name: 'cafe',
  categoryId: 21233,
  supplierId: 2026,
  unitCost: 14845.49,
  unitPrice: 7826.74,
  stockQty: 9554,
};

export const sampleWithNewData: NewProduct = {
  name: 'anti inject why',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
