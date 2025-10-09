import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vat-rate.test-samples';

import { VATRateFormService } from './vat-rate-form.service';

describe('VATRate Form Service', () => {
  let service: VATRateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VATRateFormService);
  });

  describe('Service methods', () => {
    describe('createVATRateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVATRateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            percentage: expect.any(Object),
          }),
        );
      });

      it('passing IVATRate should create a new form with FormGroup', () => {
        const formGroup = service.createVATRateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            percentage: expect.any(Object),
          }),
        );
      });
    });

    describe('getVATRate', () => {
      it('should return NewVATRate for default VATRate initial value', () => {
        const formGroup = service.createVATRateFormGroup(sampleWithNewData);

        const vATRate = service.getVATRate(formGroup) as any;

        expect(vATRate).toMatchObject(sampleWithNewData);
      });

      it('should return NewVATRate for empty VATRate initial value', () => {
        const formGroup = service.createVATRateFormGroup();

        const vATRate = service.getVATRate(formGroup) as any;

        expect(vATRate).toMatchObject({});
      });

      it('should return IVATRate', () => {
        const formGroup = service.createVATRateFormGroup(sampleWithRequiredData);

        const vATRate = service.getVATRate(formGroup) as any;

        expect(vATRate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVATRate should not enable id FormControl', () => {
        const formGroup = service.createVATRateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVATRate should disable id FormControl', () => {
        const formGroup = service.createVATRateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
