import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../recurring-transaction.test-samples';

import { RecurringTransactionFormService } from './recurring-transaction-form.service';

describe('RecurringTransaction Form Service', () => {
  let service: RecurringTransactionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecurringTransactionFormService);
  });

  describe('Service methods', () => {
    describe('createRecurringTransactionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecurringTransactionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            amount: expect.any(Object),
            frequency: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            accountId: expect.any(Object),
            currencyId: expect.any(Object),
            vatRateId: expect.any(Object),
          }),
        );
      });

      it('passing IRecurringTransaction should create a new form with FormGroup', () => {
        const formGroup = service.createRecurringTransactionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            amount: expect.any(Object),
            frequency: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            accountId: expect.any(Object),
            currencyId: expect.any(Object),
            vatRateId: expect.any(Object),
          }),
        );
      });
    });

    describe('getRecurringTransaction', () => {
      it('should return NewRecurringTransaction for default RecurringTransaction initial value', () => {
        const formGroup = service.createRecurringTransactionFormGroup(sampleWithNewData);

        const recurringTransaction = service.getRecurringTransaction(formGroup) as any;

        expect(recurringTransaction).toMatchObject(sampleWithNewData);
      });

      it('should return NewRecurringTransaction for empty RecurringTransaction initial value', () => {
        const formGroup = service.createRecurringTransactionFormGroup();

        const recurringTransaction = service.getRecurringTransaction(formGroup) as any;

        expect(recurringTransaction).toMatchObject({});
      });

      it('should return IRecurringTransaction', () => {
        const formGroup = service.createRecurringTransactionFormGroup(sampleWithRequiredData);

        const recurringTransaction = service.getRecurringTransaction(formGroup) as any;

        expect(recurringTransaction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRecurringTransaction should not enable id FormControl', () => {
        const formGroup = service.createRecurringTransactionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRecurringTransaction should disable id FormControl', () => {
        const formGroup = service.createRecurringTransactionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
