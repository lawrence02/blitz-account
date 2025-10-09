import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../chart-of-account.test-samples';

import { ChartOfAccountFormService } from './chart-of-account-form.service';

describe('ChartOfAccount Form Service', () => {
  let service: ChartOfAccountFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChartOfAccountFormService);
  });

  describe('Service methods', () => {
    describe('createChartOfAccountFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChartOfAccountFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            accountType: expect.any(Object),
            code: expect.any(Object),
            initialBalance: expect.any(Object),
            currentBalance: expect.any(Object),
          }),
        );
      });

      it('passing IChartOfAccount should create a new form with FormGroup', () => {
        const formGroup = service.createChartOfAccountFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            accountType: expect.any(Object),
            code: expect.any(Object),
            initialBalance: expect.any(Object),
            currentBalance: expect.any(Object),
          }),
        );
      });
    });

    describe('getChartOfAccount', () => {
      it('should return NewChartOfAccount for default ChartOfAccount initial value', () => {
        const formGroup = service.createChartOfAccountFormGroup(sampleWithNewData);

        const chartOfAccount = service.getChartOfAccount(formGroup) as any;

        expect(chartOfAccount).toMatchObject(sampleWithNewData);
      });

      it('should return NewChartOfAccount for empty ChartOfAccount initial value', () => {
        const formGroup = service.createChartOfAccountFormGroup();

        const chartOfAccount = service.getChartOfAccount(formGroup) as any;

        expect(chartOfAccount).toMatchObject({});
      });

      it('should return IChartOfAccount', () => {
        const formGroup = service.createChartOfAccountFormGroup(sampleWithRequiredData);

        const chartOfAccount = service.getChartOfAccount(formGroup) as any;

        expect(chartOfAccount).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IChartOfAccount should not enable id FormControl', () => {
        const formGroup = service.createChartOfAccountFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewChartOfAccount should disable id FormControl', () => {
        const formGroup = service.createChartOfAccountFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
