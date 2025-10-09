import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoice | NewInvoice> = Omit<T, 'issueDate' | 'dueDate'> & {
  issueDate?: string | null;
  dueDate?: string | null;
};

type InvoiceFormRawValue = FormValueOf<IInvoice>;

type NewInvoiceFormRawValue = FormValueOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id' | 'issueDate' | 'dueDate'>;

type InvoiceFormGroupContent = {
  id: FormControl<InvoiceFormRawValue['id'] | NewInvoice['id']>;
  clientName: FormControl<InvoiceFormRawValue['clientName']>;
  issueDate: FormControl<InvoiceFormRawValue['issueDate']>;
  dueDate: FormControl<InvoiceFormRawValue['dueDate']>;
  status: FormControl<InvoiceFormRawValue['status']>;
  currencyId: FormControl<InvoiceFormRawValue['currencyId']>;
  vatRateId: FormControl<InvoiceFormRawValue['vatRateId']>;
  totalAmount: FormControl<InvoiceFormRawValue['totalAmount']>;
  paidAmount: FormControl<InvoiceFormRawValue['paidAmount']>;
  paymentStatus: FormControl<InvoiceFormRawValue['paymentStatus']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...invoice,
    });
    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      clientName: new FormControl(invoiceRawValue.clientName, {
        validators: [Validators.required],
      }),
      issueDate: new FormControl(invoiceRawValue.issueDate, {
        validators: [Validators.required],
      }),
      dueDate: new FormControl(invoiceRawValue.dueDate),
      status: new FormControl(invoiceRawValue.status),
      currencyId: new FormControl(invoiceRawValue.currencyId),
      vatRateId: new FormControl(invoiceRawValue.vatRateId),
      totalAmount: new FormControl(invoiceRawValue.totalAmount, {
        validators: [Validators.required],
      }),
      paidAmount: new FormControl(invoiceRawValue.paidAmount),
      paymentStatus: new FormControl(invoiceRawValue.paymentStatus, {
        validators: [Validators.required],
      }),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return this.convertInvoiceRawValueToInvoice(form.getRawValue() as InvoiceFormRawValue | NewInvoiceFormRawValue);
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({ ...this.getFormDefaults(), ...invoice });
    form.reset(
      {
        ...invoiceRawValue,
        id: { value: invoiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issueDate: currentTime,
      dueDate: currentTime,
    };
  }

  private convertInvoiceRawValueToInvoice(rawInvoice: InvoiceFormRawValue | NewInvoiceFormRawValue): IInvoice | NewInvoice {
    return {
      ...rawInvoice,
      issueDate: dayjs(rawInvoice.issueDate, DATE_TIME_FORMAT),
      dueDate: dayjs(rawInvoice.dueDate, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceToInvoiceRawValue(
    invoice: IInvoice | (Partial<NewInvoice> & InvoiceFormDefaults),
  ): InvoiceFormRawValue | PartialWithRequiredKeyOf<NewInvoiceFormRawValue> {
    return {
      ...invoice,
      issueDate: invoice.issueDate ? invoice.issueDate.format(DATE_TIME_FORMAT) : undefined,
      dueDate: invoice.dueDate ? invoice.dueDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
