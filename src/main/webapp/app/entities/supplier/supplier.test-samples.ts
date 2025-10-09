import { ISupplier, NewSupplier } from './supplier.model';

export const sampleWithRequiredData: ISupplier = {
  id: 22717,
  name: 'menacing',
};

export const sampleWithPartialData: ISupplier = {
  id: 32611,
  name: 'of',
  address: 'warp which',
};

export const sampleWithFullData: ISupplier = {
  id: 3840,
  name: 'cheerfully near',
  contactNumber: 'providence ick scholarship',
  email: 'Ola42@gmail.com',
  address: 'glaring hunt',
};

export const sampleWithNewData: NewSupplier = {
  name: 'snack',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
