import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVATRate, NewVATRate } from '../vat-rate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVATRate for edit and NewVATRateFormGroupInput for create.
 */
type VATRateFormGroupInput = IVATRate | PartialWithRequiredKeyOf<NewVATRate>;

type VATRateFormDefaults = Pick<NewVATRate, 'id'>;

type VATRateFormGroupContent = {
  id: FormControl<IVATRate['id'] | NewVATRate['id']>;
  name: FormControl<IVATRate['name']>;
  percentage: FormControl<IVATRate['percentage']>;
};

export type VATRateFormGroup = FormGroup<VATRateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VATRateFormService {
  createVATRateFormGroup(vATRate: VATRateFormGroupInput = { id: null }): VATRateFormGroup {
    const vATRateRawValue = {
      ...this.getFormDefaults(),
      ...vATRate,
    };
    return new FormGroup<VATRateFormGroupContent>({
      id: new FormControl(
        { value: vATRateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(vATRateRawValue.name, {
        validators: [Validators.required],
      }),
      percentage: new FormControl(vATRateRawValue.percentage, {
        validators: [Validators.required],
      }),
    });
  }

  getVATRate(form: VATRateFormGroup): IVATRate | NewVATRate {
    return form.getRawValue() as IVATRate | NewVATRate;
  }

  resetForm(form: VATRateFormGroup, vATRate: VATRateFormGroupInput): void {
    const vATRateRawValue = { ...this.getFormDefaults(), ...vATRate };
    form.reset(
      {
        ...vATRateRawValue,
        id: { value: vATRateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VATRateFormDefaults {
    return {
      id: null,
    };
  }
}
