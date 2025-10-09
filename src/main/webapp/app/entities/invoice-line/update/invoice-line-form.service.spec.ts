import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../invoice-line.test-samples';

import { InvoiceLineFormService } from './invoice-line-form.service';

describe('InvoiceLine Form Service', () => {
  let service: InvoiceLineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InvoiceLineFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceLineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceLineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceId: expect.any(Object),
            productId: expect.any(Object),
            quantity: expect.any(Object),
            unitPrice: expect.any(Object),
            vatRateId: expect.any(Object),
          }),
        );
      });

      it('passing IInvoiceLine should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceLineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceId: expect.any(Object),
            productId: expect.any(Object),
            quantity: expect.any(Object),
            unitPrice: expect.any(Object),
            vatRateId: expect.any(Object),
          }),
        );
      });
    });

    describe('getInvoiceLine', () => {
      it('should return NewInvoiceLine for default InvoiceLine initial value', () => {
        const formGroup = service.createInvoiceLineFormGroup(sampleWithNewData);

        const invoiceLine = service.getInvoiceLine(formGroup) as any;

        expect(invoiceLine).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoiceLine for empty InvoiceLine initial value', () => {
        const formGroup = service.createInvoiceLineFormGroup();

        const invoiceLine = service.getInvoiceLine(formGroup) as any;

        expect(invoiceLine).toMatchObject({});
      });

      it('should return IInvoiceLine', () => {
        const formGroup = service.createInvoiceLineFormGroup(sampleWithRequiredData);

        const invoiceLine = service.getInvoiceLine(formGroup) as any;

        expect(invoiceLine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoiceLine should not enable id FormControl', () => {
        const formGroup = service.createInvoiceLineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInvoiceLine should disable id FormControl', () => {
        const formGroup = service.createInvoiceLineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
