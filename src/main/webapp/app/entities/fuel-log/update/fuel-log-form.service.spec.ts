import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fuel-log.test-samples';

import { FuelLogFormService } from './fuel-log-form.service';

describe('FuelLog Form Service', () => {
  let service: FuelLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FuelLogFormService);
  });

  describe('Service methods', () => {
    describe('createFuelLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFuelLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vehicleId: expect.any(Object),
            date: expect.any(Object),
            fuelVolume: expect.any(Object),
            fuelCost: expect.any(Object),
            location: expect.any(Object),
            tripId: expect.any(Object),
          }),
        );
      });

      it('passing IFuelLog should create a new form with FormGroup', () => {
        const formGroup = service.createFuelLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vehicleId: expect.any(Object),
            date: expect.any(Object),
            fuelVolume: expect.any(Object),
            fuelCost: expect.any(Object),
            location: expect.any(Object),
            tripId: expect.any(Object),
          }),
        );
      });
    });

    describe('getFuelLog', () => {
      it('should return NewFuelLog for default FuelLog initial value', () => {
        const formGroup = service.createFuelLogFormGroup(sampleWithNewData);

        const fuelLog = service.getFuelLog(formGroup) as any;

        expect(fuelLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewFuelLog for empty FuelLog initial value', () => {
        const formGroup = service.createFuelLogFormGroup();

        const fuelLog = service.getFuelLog(formGroup) as any;

        expect(fuelLog).toMatchObject({});
      });

      it('should return IFuelLog', () => {
        const formGroup = service.createFuelLogFormGroup(sampleWithRequiredData);

        const fuelLog = service.getFuelLog(formGroup) as any;

        expect(fuelLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFuelLog should not enable id FormControl', () => {
        const formGroup = service.createFuelLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFuelLog should disable id FormControl', () => {
        const formGroup = service.createFuelLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
