import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExchangeRate, NewExchangeRate } from '../exchange-rate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExchangeRate for edit and NewExchangeRateFormGroupInput for create.
 */
type ExchangeRateFormGroupInput = IExchangeRate | PartialWithRequiredKeyOf<NewExchangeRate>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExchangeRate | NewExchangeRate> = Omit<T, 'rateDate'> & {
  rateDate?: string | null;
};

type ExchangeRateFormRawValue = FormValueOf<IExchangeRate>;

type NewExchangeRateFormRawValue = FormValueOf<NewExchangeRate>;

type ExchangeRateFormDefaults = Pick<NewExchangeRate, 'id' | 'rateDate'>;

type ExchangeRateFormGroupContent = {
  id: FormControl<ExchangeRateFormRawValue['id'] | NewExchangeRate['id']>;
  baseCurrencyId: FormControl<ExchangeRateFormRawValue['baseCurrencyId']>;
  targetCurrencyId: FormControl<ExchangeRateFormRawValue['targetCurrencyId']>;
  rate: FormControl<ExchangeRateFormRawValue['rate']>;
  rateDate: FormControl<ExchangeRateFormRawValue['rateDate']>;
};

export type ExchangeRateFormGroup = FormGroup<ExchangeRateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExchangeRateFormService {
  createExchangeRateFormGroup(exchangeRate: ExchangeRateFormGroupInput = { id: null }): ExchangeRateFormGroup {
    const exchangeRateRawValue = this.convertExchangeRateToExchangeRateRawValue({
      ...this.getFormDefaults(),
      ...exchangeRate,
    });
    return new FormGroup<ExchangeRateFormGroupContent>({
      id: new FormControl(
        { value: exchangeRateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      baseCurrencyId: new FormControl(exchangeRateRawValue.baseCurrencyId, {
        validators: [Validators.required],
      }),
      targetCurrencyId: new FormControl(exchangeRateRawValue.targetCurrencyId, {
        validators: [Validators.required],
      }),
      rate: new FormControl(exchangeRateRawValue.rate, {
        validators: [Validators.required],
      }),
      rateDate: new FormControl(exchangeRateRawValue.rateDate, {
        validators: [Validators.required],
      }),
    });
  }

  getExchangeRate(form: ExchangeRateFormGroup): IExchangeRate | NewExchangeRate {
    return this.convertExchangeRateRawValueToExchangeRate(form.getRawValue() as ExchangeRateFormRawValue | NewExchangeRateFormRawValue);
  }

  resetForm(form: ExchangeRateFormGroup, exchangeRate: ExchangeRateFormGroupInput): void {
    const exchangeRateRawValue = this.convertExchangeRateToExchangeRateRawValue({ ...this.getFormDefaults(), ...exchangeRate });
    form.reset(
      {
        ...exchangeRateRawValue,
        id: { value: exchangeRateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExchangeRateFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      rateDate: currentTime,
    };
  }

  private convertExchangeRateRawValueToExchangeRate(
    rawExchangeRate: ExchangeRateFormRawValue | NewExchangeRateFormRawValue,
  ): IExchangeRate | NewExchangeRate {
    return {
      ...rawExchangeRate,
      rateDate: dayjs(rawExchangeRate.rateDate, DATE_TIME_FORMAT),
    };
  }

  private convertExchangeRateToExchangeRateRawValue(
    exchangeRate: IExchangeRate | (Partial<NewExchangeRate> & ExchangeRateFormDefaults),
  ): ExchangeRateFormRawValue | PartialWithRequiredKeyOf<NewExchangeRateFormRawValue> {
    return {
      ...exchangeRate,
      rateDate: exchangeRate.rateDate ? exchangeRate.rateDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
