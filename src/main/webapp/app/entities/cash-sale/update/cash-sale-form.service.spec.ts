import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cash-sale.test-samples';

import { CashSaleFormService } from './cash-sale-form.service';

describe('CashSale Form Service', () => {
  let service: CashSaleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CashSaleFormService);
  });

  describe('Service methods', () => {
    describe('createCashSaleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCashSaleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            productId: expect.any(Object),
            quantity: expect.any(Object),
            unitPrice: expect.any(Object),
            vatRateId: expect.any(Object),
            currencyId: expect.any(Object),
            saleDate: expect.any(Object),
          }),
        );
      });

      it('passing ICashSale should create a new form with FormGroup', () => {
        const formGroup = service.createCashSaleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            productId: expect.any(Object),
            quantity: expect.any(Object),
            unitPrice: expect.any(Object),
            vatRateId: expect.any(Object),
            currencyId: expect.any(Object),
            saleDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getCashSale', () => {
      it('should return NewCashSale for default CashSale initial value', () => {
        const formGroup = service.createCashSaleFormGroup(sampleWithNewData);

        const cashSale = service.getCashSale(formGroup) as any;

        expect(cashSale).toMatchObject(sampleWithNewData);
      });

      it('should return NewCashSale for empty CashSale initial value', () => {
        const formGroup = service.createCashSaleFormGroup();

        const cashSale = service.getCashSale(formGroup) as any;

        expect(cashSale).toMatchObject({});
      });

      it('should return ICashSale', () => {
        const formGroup = service.createCashSaleFormGroup(sampleWithRequiredData);

        const cashSale = service.getCashSale(formGroup) as any;

        expect(cashSale).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICashSale should not enable id FormControl', () => {
        const formGroup = service.createCashSaleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCashSale should disable id FormControl', () => {
        const formGroup = service.createCashSaleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
