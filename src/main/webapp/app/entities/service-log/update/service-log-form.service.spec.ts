import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../service-log.test-samples';

import { ServiceLogFormService } from './service-log-form.service';

describe('ServiceLog Form Service', () => {
  let service: ServiceLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceLogFormService);
  });

  describe('Service methods', () => {
    describe('createServiceLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vehicleId: expect.any(Object),
            serviceDate: expect.any(Object),
            description: expect.any(Object),
            cost: expect.any(Object),
            mileageAtService: expect.any(Object),
            supplierId: expect.any(Object),
          }),
        );
      });

      it('passing IServiceLog should create a new form with FormGroup', () => {
        const formGroup = service.createServiceLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vehicleId: expect.any(Object),
            serviceDate: expect.any(Object),
            description: expect.any(Object),
            cost: expect.any(Object),
            mileageAtService: expect.any(Object),
            supplierId: expect.any(Object),
          }),
        );
      });
    });

    describe('getServiceLog', () => {
      it('should return NewServiceLog for default ServiceLog initial value', () => {
        const formGroup = service.createServiceLogFormGroup(sampleWithNewData);

        const serviceLog = service.getServiceLog(formGroup) as any;

        expect(serviceLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceLog for empty ServiceLog initial value', () => {
        const formGroup = service.createServiceLogFormGroup();

        const serviceLog = service.getServiceLog(formGroup) as any;

        expect(serviceLog).toMatchObject({});
      });

      it('should return IServiceLog', () => {
        const formGroup = service.createServiceLogFormGroup(sampleWithRequiredData);

        const serviceLog = service.getServiceLog(formGroup) as any;

        expect(serviceLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceLog should not enable id FormControl', () => {
        const formGroup = service.createServiceLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceLog should disable id FormControl', () => {
        const formGroup = service.createServiceLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
