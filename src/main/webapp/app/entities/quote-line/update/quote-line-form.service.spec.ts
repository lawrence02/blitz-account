import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../quote-line.test-samples';

import { QuoteLineFormService } from './quote-line-form.service';

describe('QuoteLine Form Service', () => {
  let service: QuoteLineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuoteLineFormService);
  });

  describe('Service methods', () => {
    describe('createQuoteLineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuoteLineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quoteId: expect.any(Object),
            productId: expect.any(Object),
            quantity: expect.any(Object),
            unitPrice: expect.any(Object),
            vatRateId: expect.any(Object),
          }),
        );
      });

      it('passing IQuoteLine should create a new form with FormGroup', () => {
        const formGroup = service.createQuoteLineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quoteId: expect.any(Object),
            productId: expect.any(Object),
            quantity: expect.any(Object),
            unitPrice: expect.any(Object),
            vatRateId: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuoteLine', () => {
      it('should return NewQuoteLine for default QuoteLine initial value', () => {
        const formGroup = service.createQuoteLineFormGroup(sampleWithNewData);

        const quoteLine = service.getQuoteLine(formGroup) as any;

        expect(quoteLine).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuoteLine for empty QuoteLine initial value', () => {
        const formGroup = service.createQuoteLineFormGroup();

        const quoteLine = service.getQuoteLine(formGroup) as any;

        expect(quoteLine).toMatchObject({});
      });

      it('should return IQuoteLine', () => {
        const formGroup = service.createQuoteLineFormGroup(sampleWithRequiredData);

        const quoteLine = service.getQuoteLine(formGroup) as any;

        expect(quoteLine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuoteLine should not enable id FormControl', () => {
        const formGroup = service.createQuoteLineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuoteLine should disable id FormControl', () => {
        const formGroup = service.createQuoteLineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
