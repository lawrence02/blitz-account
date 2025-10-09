import { IVehicle, NewVehicle } from './vehicle.model';

export const sampleWithRequiredData: IVehicle = {
  id: 7486,
  name: 'snack',
  licensePlate: 'who hammock scent',
  status: 'IDLE',
};

export const sampleWithPartialData: IVehicle = {
  id: 54,
  name: 'which violin finding',
  licensePlate: 'inquisitively daintily wherever',
  currentMileage: 5555.19,
  status: 'IDLE',
};

export const sampleWithFullData: IVehicle = {
  id: 12947,
  name: 'eek boo broken',
  licensePlate: 'busily',
  type: 'deploy',
  currentMileage: 28199.24,
  status: 'AVAILABLE',
};

export const sampleWithNewData: NewVehicle = {
  name: 'tremendously glisten',
  licensePlate: 'but essence',
  status: 'MAINTENANCE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
