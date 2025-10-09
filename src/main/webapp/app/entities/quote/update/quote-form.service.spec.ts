import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../quote.test-samples';

import { QuoteFormService } from './quote-form.service';

describe('Quote Form Service', () => {
  let service: QuoteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuoteFormService);
  });

  describe('Service methods', () => {
    describe('createQuoteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuoteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            clientName: expect.any(Object),
            issueDate: expect.any(Object),
            status: expect.any(Object),
            currencyId: expect.any(Object),
            vatRateId: expect.any(Object),
            totalAmount: expect.any(Object),
          }),
        );
      });

      it('passing IQuote should create a new form with FormGroup', () => {
        const formGroup = service.createQuoteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            clientName: expect.any(Object),
            issueDate: expect.any(Object),
            status: expect.any(Object),
            currencyId: expect.any(Object),
            vatRateId: expect.any(Object),
            totalAmount: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuote', () => {
      it('should return NewQuote for default Quote initial value', () => {
        const formGroup = service.createQuoteFormGroup(sampleWithNewData);

        const quote = service.getQuote(formGroup) as any;

        expect(quote).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuote for empty Quote initial value', () => {
        const formGroup = service.createQuoteFormGroup();

        const quote = service.getQuote(formGroup) as any;

        expect(quote).toMatchObject({});
      });

      it('should return IQuote', () => {
        const formGroup = service.createQuoteFormGroup(sampleWithRequiredData);

        const quote = service.getQuote(formGroup) as any;

        expect(quote).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuote should not enable id FormControl', () => {
        const formGroup = service.createQuoteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuote should disable id FormControl', () => {
        const formGroup = service.createQuoteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
