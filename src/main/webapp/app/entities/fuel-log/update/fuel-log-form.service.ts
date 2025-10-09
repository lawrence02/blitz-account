import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFuelLog, NewFuelLog } from '../fuel-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFuelLog for edit and NewFuelLogFormGroupInput for create.
 */
type FuelLogFormGroupInput = IFuelLog | PartialWithRequiredKeyOf<NewFuelLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFuelLog | NewFuelLog> = Omit<T, 'date'> & {
  date?: string | null;
};

type FuelLogFormRawValue = FormValueOf<IFuelLog>;

type NewFuelLogFormRawValue = FormValueOf<NewFuelLog>;

type FuelLogFormDefaults = Pick<NewFuelLog, 'id' | 'date'>;

type FuelLogFormGroupContent = {
  id: FormControl<FuelLogFormRawValue['id'] | NewFuelLog['id']>;
  vehicleId: FormControl<FuelLogFormRawValue['vehicleId']>;
  date: FormControl<FuelLogFormRawValue['date']>;
  fuelVolume: FormControl<FuelLogFormRawValue['fuelVolume']>;
  fuelCost: FormControl<FuelLogFormRawValue['fuelCost']>;
  location: FormControl<FuelLogFormRawValue['location']>;
  tripId: FormControl<FuelLogFormRawValue['tripId']>;
};

export type FuelLogFormGroup = FormGroup<FuelLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FuelLogFormService {
  createFuelLogFormGroup(fuelLog: FuelLogFormGroupInput = { id: null }): FuelLogFormGroup {
    const fuelLogRawValue = this.convertFuelLogToFuelLogRawValue({
      ...this.getFormDefaults(),
      ...fuelLog,
    });
    return new FormGroup<FuelLogFormGroupContent>({
      id: new FormControl(
        { value: fuelLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      vehicleId: new FormControl(fuelLogRawValue.vehicleId, {
        validators: [Validators.required],
      }),
      date: new FormControl(fuelLogRawValue.date, {
        validators: [Validators.required],
      }),
      fuelVolume: new FormControl(fuelLogRawValue.fuelVolume),
      fuelCost: new FormControl(fuelLogRawValue.fuelCost),
      location: new FormControl(fuelLogRawValue.location),
      tripId: new FormControl(fuelLogRawValue.tripId),
    });
  }

  getFuelLog(form: FuelLogFormGroup): IFuelLog | NewFuelLog {
    return this.convertFuelLogRawValueToFuelLog(form.getRawValue() as FuelLogFormRawValue | NewFuelLogFormRawValue);
  }

  resetForm(form: FuelLogFormGroup, fuelLog: FuelLogFormGroupInput): void {
    const fuelLogRawValue = this.convertFuelLogToFuelLogRawValue({ ...this.getFormDefaults(), ...fuelLog });
    form.reset(
      {
        ...fuelLogRawValue,
        id: { value: fuelLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FuelLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
    };
  }

  private convertFuelLogRawValueToFuelLog(rawFuelLog: FuelLogFormRawValue | NewFuelLogFormRawValue): IFuelLog | NewFuelLog {
    return {
      ...rawFuelLog,
      date: dayjs(rawFuelLog.date, DATE_TIME_FORMAT),
    };
  }

  private convertFuelLogToFuelLogRawValue(
    fuelLog: IFuelLog | (Partial<NewFuelLog> & FuelLogFormDefaults),
  ): FuelLogFormRawValue | PartialWithRequiredKeyOf<NewFuelLogFormRawValue> {
    return {
      ...fuelLog,
      date: fuelLog.date ? fuelLog.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
