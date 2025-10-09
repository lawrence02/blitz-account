import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRecurringTransaction, NewRecurringTransaction } from '../recurring-transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecurringTransaction for edit and NewRecurringTransactionFormGroupInput for create.
 */
type RecurringTransactionFormGroupInput = IRecurringTransaction | PartialWithRequiredKeyOf<NewRecurringTransaction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRecurringTransaction | NewRecurringTransaction> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type RecurringTransactionFormRawValue = FormValueOf<IRecurringTransaction>;

type NewRecurringTransactionFormRawValue = FormValueOf<NewRecurringTransaction>;

type RecurringTransactionFormDefaults = Pick<NewRecurringTransaction, 'id' | 'startDate' | 'endDate'>;

type RecurringTransactionFormGroupContent = {
  id: FormControl<RecurringTransactionFormRawValue['id'] | NewRecurringTransaction['id']>;
  name: FormControl<RecurringTransactionFormRawValue['name']>;
  amount: FormControl<RecurringTransactionFormRawValue['amount']>;
  frequency: FormControl<RecurringTransactionFormRawValue['frequency']>;
  startDate: FormControl<RecurringTransactionFormRawValue['startDate']>;
  endDate: FormControl<RecurringTransactionFormRawValue['endDate']>;
  accountId: FormControl<RecurringTransactionFormRawValue['accountId']>;
  currencyId: FormControl<RecurringTransactionFormRawValue['currencyId']>;
  vatRateId: FormControl<RecurringTransactionFormRawValue['vatRateId']>;
};

export type RecurringTransactionFormGroup = FormGroup<RecurringTransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RecurringTransactionFormService {
  createRecurringTransactionFormGroup(
    recurringTransaction: RecurringTransactionFormGroupInput = { id: null },
  ): RecurringTransactionFormGroup {
    const recurringTransactionRawValue = this.convertRecurringTransactionToRecurringTransactionRawValue({
      ...this.getFormDefaults(),
      ...recurringTransaction,
    });
    return new FormGroup<RecurringTransactionFormGroupContent>({
      id: new FormControl(
        { value: recurringTransactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(recurringTransactionRawValue.name, {
        validators: [Validators.required],
      }),
      amount: new FormControl(recurringTransactionRawValue.amount, {
        validators: [Validators.required],
      }),
      frequency: new FormControl(recurringTransactionRawValue.frequency, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(recurringTransactionRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(recurringTransactionRawValue.endDate),
      accountId: new FormControl(recurringTransactionRawValue.accountId),
      currencyId: new FormControl(recurringTransactionRawValue.currencyId),
      vatRateId: new FormControl(recurringTransactionRawValue.vatRateId),
    });
  }

  getRecurringTransaction(form: RecurringTransactionFormGroup): IRecurringTransaction | NewRecurringTransaction {
    return this.convertRecurringTransactionRawValueToRecurringTransaction(
      form.getRawValue() as RecurringTransactionFormRawValue | NewRecurringTransactionFormRawValue,
    );
  }

  resetForm(form: RecurringTransactionFormGroup, recurringTransaction: RecurringTransactionFormGroupInput): void {
    const recurringTransactionRawValue = this.convertRecurringTransactionToRecurringTransactionRawValue({
      ...this.getFormDefaults(),
      ...recurringTransaction,
    });
    form.reset(
      {
        ...recurringTransactionRawValue,
        id: { value: recurringTransactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RecurringTransactionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertRecurringTransactionRawValueToRecurringTransaction(
    rawRecurringTransaction: RecurringTransactionFormRawValue | NewRecurringTransactionFormRawValue,
  ): IRecurringTransaction | NewRecurringTransaction {
    return {
      ...rawRecurringTransaction,
      startDate: dayjs(rawRecurringTransaction.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawRecurringTransaction.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertRecurringTransactionToRecurringTransactionRawValue(
    recurringTransaction: IRecurringTransaction | (Partial<NewRecurringTransaction> & RecurringTransactionFormDefaults),
  ): RecurringTransactionFormRawValue | PartialWithRequiredKeyOf<NewRecurringTransactionFormRawValue> {
    return {
      ...recurringTransaction,
      startDate: recurringTransaction.startDate ? recurringTransaction.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: recurringTransaction.endDate ? recurringTransaction.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
