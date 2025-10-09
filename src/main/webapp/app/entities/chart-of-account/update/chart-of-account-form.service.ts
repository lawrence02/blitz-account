import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IChartOfAccount, NewChartOfAccount } from '../chart-of-account.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChartOfAccount for edit and NewChartOfAccountFormGroupInput for create.
 */
type ChartOfAccountFormGroupInput = IChartOfAccount | PartialWithRequiredKeyOf<NewChartOfAccount>;

type ChartOfAccountFormDefaults = Pick<NewChartOfAccount, 'id'>;

type ChartOfAccountFormGroupContent = {
  id: FormControl<IChartOfAccount['id'] | NewChartOfAccount['id']>;
  name: FormControl<IChartOfAccount['name']>;
  accountType: FormControl<IChartOfAccount['accountType']>;
  code: FormControl<IChartOfAccount['code']>;
  initialBalance: FormControl<IChartOfAccount['initialBalance']>;
  currentBalance: FormControl<IChartOfAccount['currentBalance']>;
};

export type ChartOfAccountFormGroup = FormGroup<ChartOfAccountFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChartOfAccountFormService {
  createChartOfAccountFormGroup(chartOfAccount: ChartOfAccountFormGroupInput = { id: null }): ChartOfAccountFormGroup {
    const chartOfAccountRawValue = {
      ...this.getFormDefaults(),
      ...chartOfAccount,
    };
    return new FormGroup<ChartOfAccountFormGroupContent>({
      id: new FormControl(
        { value: chartOfAccountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(chartOfAccountRawValue.name, {
        validators: [Validators.required],
      }),
      accountType: new FormControl(chartOfAccountRawValue.accountType, {
        validators: [Validators.required],
      }),
      code: new FormControl(chartOfAccountRawValue.code, {
        validators: [Validators.required],
      }),
      initialBalance: new FormControl(chartOfAccountRawValue.initialBalance),
      currentBalance: new FormControl(chartOfAccountRawValue.currentBalance),
    });
  }

  getChartOfAccount(form: ChartOfAccountFormGroup): IChartOfAccount | NewChartOfAccount {
    return form.getRawValue() as IChartOfAccount | NewChartOfAccount;
  }

  resetForm(form: ChartOfAccountFormGroup, chartOfAccount: ChartOfAccountFormGroupInput): void {
    const chartOfAccountRawValue = { ...this.getFormDefaults(), ...chartOfAccount };
    form.reset(
      {
        ...chartOfAccountRawValue,
        id: { value: chartOfAccountRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ChartOfAccountFormDefaults {
    return {
      id: null,
    };
  }
}
