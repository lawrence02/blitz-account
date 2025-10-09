import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 8899,
  name: 'blushing institute',
  role: 'requirement catalyst glossy',
};

export const sampleWithPartialData: IEmployee = {
  id: 4463,
  name: 'simple',
  role: 'silently',
  contactNumber: 'um',
  email: 'Mertie44@hotmail.com',
};

export const sampleWithFullData: IEmployee = {
  id: 19019,
  name: 'sharply questionably softly',
  role: 'yak near',
  contactNumber: 'once',
  email: 'Melvina14@hotmail.com',
};

export const sampleWithNewData: NewEmployee = {
  name: 'unless',
  role: 'annually whenever boo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
