import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICashSale, NewCashSale } from '../cash-sale.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICashSale for edit and NewCashSaleFormGroupInput for create.
 */
type CashSaleFormGroupInput = ICashSale | PartialWithRequiredKeyOf<NewCashSale>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICashSale | NewCashSale> = Omit<T, 'saleDate'> & {
  saleDate?: string | null;
};

type CashSaleFormRawValue = FormValueOf<ICashSale>;

type NewCashSaleFormRawValue = FormValueOf<NewCashSale>;

type CashSaleFormDefaults = Pick<NewCashSale, 'id' | 'saleDate'>;

type CashSaleFormGroupContent = {
  id: FormControl<CashSaleFormRawValue['id'] | NewCashSale['id']>;
  productId: FormControl<CashSaleFormRawValue['productId']>;
  quantity: FormControl<CashSaleFormRawValue['quantity']>;
  unitPrice: FormControl<CashSaleFormRawValue['unitPrice']>;
  vatRateId: FormControl<CashSaleFormRawValue['vatRateId']>;
  currencyId: FormControl<CashSaleFormRawValue['currencyId']>;
  saleDate: FormControl<CashSaleFormRawValue['saleDate']>;
};

export type CashSaleFormGroup = FormGroup<CashSaleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CashSaleFormService {
  createCashSaleFormGroup(cashSale: CashSaleFormGroupInput = { id: null }): CashSaleFormGroup {
    const cashSaleRawValue = this.convertCashSaleToCashSaleRawValue({
      ...this.getFormDefaults(),
      ...cashSale,
    });
    return new FormGroup<CashSaleFormGroupContent>({
      id: new FormControl(
        { value: cashSaleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      productId: new FormControl(cashSaleRawValue.productId),
      quantity: new FormControl(cashSaleRawValue.quantity),
      unitPrice: new FormControl(cashSaleRawValue.unitPrice),
      vatRateId: new FormControl(cashSaleRawValue.vatRateId),
      currencyId: new FormControl(cashSaleRawValue.currencyId),
      saleDate: new FormControl(cashSaleRawValue.saleDate),
    });
  }

  getCashSale(form: CashSaleFormGroup): ICashSale | NewCashSale {
    return this.convertCashSaleRawValueToCashSale(form.getRawValue() as CashSaleFormRawValue | NewCashSaleFormRawValue);
  }

  resetForm(form: CashSaleFormGroup, cashSale: CashSaleFormGroupInput): void {
    const cashSaleRawValue = this.convertCashSaleToCashSaleRawValue({ ...this.getFormDefaults(), ...cashSale });
    form.reset(
      {
        ...cashSaleRawValue,
        id: { value: cashSaleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CashSaleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      saleDate: currentTime,
    };
  }

  private convertCashSaleRawValueToCashSale(rawCashSale: CashSaleFormRawValue | NewCashSaleFormRawValue): ICashSale | NewCashSale {
    return {
      ...rawCashSale,
      saleDate: dayjs(rawCashSale.saleDate, DATE_TIME_FORMAT),
    };
  }

  private convertCashSaleToCashSaleRawValue(
    cashSale: ICashSale | (Partial<NewCashSale> & CashSaleFormDefaults),
  ): CashSaleFormRawValue | PartialWithRequiredKeyOf<NewCashSaleFormRawValue> {
    return {
      ...cashSale,
      saleDate: cashSale.saleDate ? cashSale.saleDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
