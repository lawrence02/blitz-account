import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInvoiceLine, NewInvoiceLine } from '../invoice-line.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceLine for edit and NewInvoiceLineFormGroupInput for create.
 */
type InvoiceLineFormGroupInput = IInvoiceLine | PartialWithRequiredKeyOf<NewInvoiceLine>;

type InvoiceLineFormDefaults = Pick<NewInvoiceLine, 'id'>;

type InvoiceLineFormGroupContent = {
  id: FormControl<IInvoiceLine['id'] | NewInvoiceLine['id']>;
  invoiceId: FormControl<IInvoiceLine['invoiceId']>;
  productId: FormControl<IInvoiceLine['productId']>;
  quantity: FormControl<IInvoiceLine['quantity']>;
  unitPrice: FormControl<IInvoiceLine['unitPrice']>;
  vatRateId: FormControl<IInvoiceLine['vatRateId']>;
};

export type InvoiceLineFormGroup = FormGroup<InvoiceLineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceLineFormService {
  createInvoiceLineFormGroup(invoiceLine: InvoiceLineFormGroupInput = { id: null }): InvoiceLineFormGroup {
    const invoiceLineRawValue = {
      ...this.getFormDefaults(),
      ...invoiceLine,
    };
    return new FormGroup<InvoiceLineFormGroupContent>({
      id: new FormControl(
        { value: invoiceLineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      invoiceId: new FormControl(invoiceLineRawValue.invoiceId, {
        validators: [Validators.required],
      }),
      productId: new FormControl(invoiceLineRawValue.productId),
      quantity: new FormControl(invoiceLineRawValue.quantity),
      unitPrice: new FormControl(invoiceLineRawValue.unitPrice),
      vatRateId: new FormControl(invoiceLineRawValue.vatRateId),
    });
  }

  getInvoiceLine(form: InvoiceLineFormGroup): IInvoiceLine | NewInvoiceLine {
    return form.getRawValue() as IInvoiceLine | NewInvoiceLine;
  }

  resetForm(form: InvoiceLineFormGroup, invoiceLine: InvoiceLineFormGroupInput): void {
    const invoiceLineRawValue = { ...this.getFormDefaults(), ...invoiceLine };
    form.reset(
      {
        ...invoiceLineRawValue,
        id: { value: invoiceLineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceLineFormDefaults {
    return {
      id: null,
    };
  }
}
