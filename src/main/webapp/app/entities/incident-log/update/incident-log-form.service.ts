import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIncidentLog, NewIncidentLog } from '../incident-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIncidentLog for edit and NewIncidentLogFormGroupInput for create.
 */
type IncidentLogFormGroupInput = IIncidentLog | PartialWithRequiredKeyOf<NewIncidentLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIncidentLog | NewIncidentLog> = Omit<T, 'incidentDate'> & {
  incidentDate?: string | null;
};

type IncidentLogFormRawValue = FormValueOf<IIncidentLog>;

type NewIncidentLogFormRawValue = FormValueOf<NewIncidentLog>;

type IncidentLogFormDefaults = Pick<NewIncidentLog, 'id' | 'incidentDate'>;

type IncidentLogFormGroupContent = {
  id: FormControl<IncidentLogFormRawValue['id'] | NewIncidentLog['id']>;
  vehicleId: FormControl<IncidentLogFormRawValue['vehicleId']>;
  tripId: FormControl<IncidentLogFormRawValue['tripId']>;
  incidentDate: FormControl<IncidentLogFormRawValue['incidentDate']>;
  type: FormControl<IncidentLogFormRawValue['type']>;
  description: FormControl<IncidentLogFormRawValue['description']>;
  cost: FormControl<IncidentLogFormRawValue['cost']>;
};

export type IncidentLogFormGroup = FormGroup<IncidentLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IncidentLogFormService {
  createIncidentLogFormGroup(incidentLog: IncidentLogFormGroupInput = { id: null }): IncidentLogFormGroup {
    const incidentLogRawValue = this.convertIncidentLogToIncidentLogRawValue({
      ...this.getFormDefaults(),
      ...incidentLog,
    });
    return new FormGroup<IncidentLogFormGroupContent>({
      id: new FormControl(
        { value: incidentLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      vehicleId: new FormControl(incidentLogRawValue.vehicleId, {
        validators: [Validators.required],
      }),
      tripId: new FormControl(incidentLogRawValue.tripId),
      incidentDate: new FormControl(incidentLogRawValue.incidentDate, {
        validators: [Validators.required],
      }),
      type: new FormControl(incidentLogRawValue.type),
      description: new FormControl(incidentLogRawValue.description),
      cost: new FormControl(incidentLogRawValue.cost),
    });
  }

  getIncidentLog(form: IncidentLogFormGroup): IIncidentLog | NewIncidentLog {
    return this.convertIncidentLogRawValueToIncidentLog(form.getRawValue() as IncidentLogFormRawValue | NewIncidentLogFormRawValue);
  }

  resetForm(form: IncidentLogFormGroup, incidentLog: IncidentLogFormGroupInput): void {
    const incidentLogRawValue = this.convertIncidentLogToIncidentLogRawValue({ ...this.getFormDefaults(), ...incidentLog });
    form.reset(
      {
        ...incidentLogRawValue,
        id: { value: incidentLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): IncidentLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      incidentDate: currentTime,
    };
  }

  private convertIncidentLogRawValueToIncidentLog(
    rawIncidentLog: IncidentLogFormRawValue | NewIncidentLogFormRawValue,
  ): IIncidentLog | NewIncidentLog {
    return {
      ...rawIncidentLog,
      incidentDate: dayjs(rawIncidentLog.incidentDate, DATE_TIME_FORMAT),
    };
  }

  private convertIncidentLogToIncidentLogRawValue(
    incidentLog: IIncidentLog | (Partial<NewIncidentLog> & IncidentLogFormDefaults),
  ): IncidentLogFormRawValue | PartialWithRequiredKeyOf<NewIncidentLogFormRawValue> {
    return {
      ...incidentLog,
      incidentDate: incidentLog.incidentDate ? incidentLog.incidentDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
