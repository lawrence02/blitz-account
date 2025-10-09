import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IQuote, NewQuote } from '../quote.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuote for edit and NewQuoteFormGroupInput for create.
 */
type QuoteFormGroupInput = IQuote | PartialWithRequiredKeyOf<NewQuote>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQuote | NewQuote> = Omit<T, 'issueDate'> & {
  issueDate?: string | null;
};

type QuoteFormRawValue = FormValueOf<IQuote>;

type NewQuoteFormRawValue = FormValueOf<NewQuote>;

type QuoteFormDefaults = Pick<NewQuote, 'id' | 'issueDate'>;

type QuoteFormGroupContent = {
  id: FormControl<QuoteFormRawValue['id'] | NewQuote['id']>;
  clientName: FormControl<QuoteFormRawValue['clientName']>;
  issueDate: FormControl<QuoteFormRawValue['issueDate']>;
  status: FormControl<QuoteFormRawValue['status']>;
  currencyId: FormControl<QuoteFormRawValue['currencyId']>;
  vatRateId: FormControl<QuoteFormRawValue['vatRateId']>;
  totalAmount: FormControl<QuoteFormRawValue['totalAmount']>;
};

export type QuoteFormGroup = FormGroup<QuoteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuoteFormService {
  createQuoteFormGroup(quote: QuoteFormGroupInput = { id: null }): QuoteFormGroup {
    const quoteRawValue = this.convertQuoteToQuoteRawValue({
      ...this.getFormDefaults(),
      ...quote,
    });
    return new FormGroup<QuoteFormGroupContent>({
      id: new FormControl(
        { value: quoteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      clientName: new FormControl(quoteRawValue.clientName, {
        validators: [Validators.required],
      }),
      issueDate: new FormControl(quoteRawValue.issueDate, {
        validators: [Validators.required],
      }),
      status: new FormControl(quoteRawValue.status, {
        validators: [Validators.required],
      }),
      currencyId: new FormControl(quoteRawValue.currencyId),
      vatRateId: new FormControl(quoteRawValue.vatRateId),
      totalAmount: new FormControl(quoteRawValue.totalAmount),
    });
  }

  getQuote(form: QuoteFormGroup): IQuote | NewQuote {
    return this.convertQuoteRawValueToQuote(form.getRawValue() as QuoteFormRawValue | NewQuoteFormRawValue);
  }

  resetForm(form: QuoteFormGroup, quote: QuoteFormGroupInput): void {
    const quoteRawValue = this.convertQuoteToQuoteRawValue({ ...this.getFormDefaults(), ...quote });
    form.reset(
      {
        ...quoteRawValue,
        id: { value: quoteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuoteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issueDate: currentTime,
    };
  }

  private convertQuoteRawValueToQuote(rawQuote: QuoteFormRawValue | NewQuoteFormRawValue): IQuote | NewQuote {
    return {
      ...rawQuote,
      issueDate: dayjs(rawQuote.issueDate, DATE_TIME_FORMAT),
    };
  }

  private convertQuoteToQuoteRawValue(
    quote: IQuote | (Partial<NewQuote> & QuoteFormDefaults),
  ): QuoteFormRawValue | PartialWithRequiredKeyOf<NewQuoteFormRawValue> {
    return {
      ...quote,
      issueDate: quote.issueDate ? quote.issueDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
