import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBankTransaction, NewBankTransaction } from '../bank-transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBankTransaction for edit and NewBankTransactionFormGroupInput for create.
 */
type BankTransactionFormGroupInput = IBankTransaction | PartialWithRequiredKeyOf<NewBankTransaction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBankTransaction | NewBankTransaction> = Omit<T, 'transactionDate'> & {
  transactionDate?: string | null;
};

type BankTransactionFormRawValue = FormValueOf<IBankTransaction>;

type NewBankTransactionFormRawValue = FormValueOf<NewBankTransaction>;

type BankTransactionFormDefaults = Pick<NewBankTransaction, 'id' | 'transactionDate'>;

type BankTransactionFormGroupContent = {
  id: FormControl<BankTransactionFormRawValue['id'] | NewBankTransaction['id']>;
  bankAccountId: FormControl<BankTransactionFormRawValue['bankAccountId']>;
  transactionDate: FormControl<BankTransactionFormRawValue['transactionDate']>;
  reference: FormControl<BankTransactionFormRawValue['reference']>;
  amount: FormControl<BankTransactionFormRawValue['amount']>;
  direction: FormControl<BankTransactionFormRawValue['direction']>;
  relatedPaymentId: FormControl<BankTransactionFormRawValue['relatedPaymentId']>;
  description: FormControl<BankTransactionFormRawValue['description']>;
};

export type BankTransactionFormGroup = FormGroup<BankTransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BankTransactionFormService {
  createBankTransactionFormGroup(bankTransaction: BankTransactionFormGroupInput = { id: null }): BankTransactionFormGroup {
    const bankTransactionRawValue = this.convertBankTransactionToBankTransactionRawValue({
      ...this.getFormDefaults(),
      ...bankTransaction,
    });
    return new FormGroup<BankTransactionFormGroupContent>({
      id: new FormControl(
        { value: bankTransactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      bankAccountId: new FormControl(bankTransactionRawValue.bankAccountId, {
        validators: [Validators.required],
      }),
      transactionDate: new FormControl(bankTransactionRawValue.transactionDate, {
        validators: [Validators.required],
      }),
      reference: new FormControl(bankTransactionRawValue.reference),
      amount: new FormControl(bankTransactionRawValue.amount, {
        validators: [Validators.required],
      }),
      direction: new FormControl(bankTransactionRawValue.direction, {
        validators: [Validators.required],
      }),
      relatedPaymentId: new FormControl(bankTransactionRawValue.relatedPaymentId),
      description: new FormControl(bankTransactionRawValue.description),
    });
  }

  getBankTransaction(form: BankTransactionFormGroup): IBankTransaction | NewBankTransaction {
    return this.convertBankTransactionRawValueToBankTransaction(
      form.getRawValue() as BankTransactionFormRawValue | NewBankTransactionFormRawValue,
    );
  }

  resetForm(form: BankTransactionFormGroup, bankTransaction: BankTransactionFormGroupInput): void {
    const bankTransactionRawValue = this.convertBankTransactionToBankTransactionRawValue({ ...this.getFormDefaults(), ...bankTransaction });
    form.reset(
      {
        ...bankTransactionRawValue,
        id: { value: bankTransactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BankTransactionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      transactionDate: currentTime,
    };
  }

  private convertBankTransactionRawValueToBankTransaction(
    rawBankTransaction: BankTransactionFormRawValue | NewBankTransactionFormRawValue,
  ): IBankTransaction | NewBankTransaction {
    return {
      ...rawBankTransaction,
      transactionDate: dayjs(rawBankTransaction.transactionDate, DATE_TIME_FORMAT),
    };
  }

  private convertBankTransactionToBankTransactionRawValue(
    bankTransaction: IBankTransaction | (Partial<NewBankTransaction> & BankTransactionFormDefaults),
  ): BankTransactionFormRawValue | PartialWithRequiredKeyOf<NewBankTransactionFormRawValue> {
    return {
      ...bankTransaction,
      transactionDate: bankTransaction.transactionDate ? bankTransaction.transactionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
