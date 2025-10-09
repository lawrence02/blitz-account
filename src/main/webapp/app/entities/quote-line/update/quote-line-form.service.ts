import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IQuoteLine, NewQuoteLine } from '../quote-line.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuoteLine for edit and NewQuoteLineFormGroupInput for create.
 */
type QuoteLineFormGroupInput = IQuoteLine | PartialWithRequiredKeyOf<NewQuoteLine>;

type QuoteLineFormDefaults = Pick<NewQuoteLine, 'id'>;

type QuoteLineFormGroupContent = {
  id: FormControl<IQuoteLine['id'] | NewQuoteLine['id']>;
  quoteId: FormControl<IQuoteLine['quoteId']>;
  productId: FormControl<IQuoteLine['productId']>;
  quantity: FormControl<IQuoteLine['quantity']>;
  unitPrice: FormControl<IQuoteLine['unitPrice']>;
  vatRateId: FormControl<IQuoteLine['vatRateId']>;
};

export type QuoteLineFormGroup = FormGroup<QuoteLineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuoteLineFormService {
  createQuoteLineFormGroup(quoteLine: QuoteLineFormGroupInput = { id: null }): QuoteLineFormGroup {
    const quoteLineRawValue = {
      ...this.getFormDefaults(),
      ...quoteLine,
    };
    return new FormGroup<QuoteLineFormGroupContent>({
      id: new FormControl(
        { value: quoteLineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quoteId: new FormControl(quoteLineRawValue.quoteId, {
        validators: [Validators.required],
      }),
      productId: new FormControl(quoteLineRawValue.productId),
      quantity: new FormControl(quoteLineRawValue.quantity),
      unitPrice: new FormControl(quoteLineRawValue.unitPrice),
      vatRateId: new FormControl(quoteLineRawValue.vatRateId),
    });
  }

  getQuoteLine(form: QuoteLineFormGroup): IQuoteLine | NewQuoteLine {
    return form.getRawValue() as IQuoteLine | NewQuoteLine;
  }

  resetForm(form: QuoteLineFormGroup, quoteLine: QuoteLineFormGroupInput): void {
    const quoteLineRawValue = { ...this.getFormDefaults(), ...quoteLine };
    form.reset(
      {
        ...quoteLineRawValue,
        id: { value: quoteLineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuoteLineFormDefaults {
    return {
      id: null,
    };
  }
}
