import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IServiceLog, NewServiceLog } from '../service-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceLog for edit and NewServiceLogFormGroupInput for create.
 */
type ServiceLogFormGroupInput = IServiceLog | PartialWithRequiredKeyOf<NewServiceLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IServiceLog | NewServiceLog> = Omit<T, 'serviceDate'> & {
  serviceDate?: string | null;
};

type ServiceLogFormRawValue = FormValueOf<IServiceLog>;

type NewServiceLogFormRawValue = FormValueOf<NewServiceLog>;

type ServiceLogFormDefaults = Pick<NewServiceLog, 'id' | 'serviceDate'>;

type ServiceLogFormGroupContent = {
  id: FormControl<ServiceLogFormRawValue['id'] | NewServiceLog['id']>;
  vehicleId: FormControl<ServiceLogFormRawValue['vehicleId']>;
  serviceDate: FormControl<ServiceLogFormRawValue['serviceDate']>;
  description: FormControl<ServiceLogFormRawValue['description']>;
  cost: FormControl<ServiceLogFormRawValue['cost']>;
  mileageAtService: FormControl<ServiceLogFormRawValue['mileageAtService']>;
  supplierId: FormControl<ServiceLogFormRawValue['supplierId']>;
};

export type ServiceLogFormGroup = FormGroup<ServiceLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServiceLogFormService {
  createServiceLogFormGroup(serviceLog: ServiceLogFormGroupInput = { id: null }): ServiceLogFormGroup {
    const serviceLogRawValue = this.convertServiceLogToServiceLogRawValue({
      ...this.getFormDefaults(),
      ...serviceLog,
    });
    return new FormGroup<ServiceLogFormGroupContent>({
      id: new FormControl(
        { value: serviceLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      vehicleId: new FormControl(serviceLogRawValue.vehicleId, {
        validators: [Validators.required],
      }),
      serviceDate: new FormControl(serviceLogRawValue.serviceDate, {
        validators: [Validators.required],
      }),
      description: new FormControl(serviceLogRawValue.description),
      cost: new FormControl(serviceLogRawValue.cost),
      mileageAtService: new FormControl(serviceLogRawValue.mileageAtService),
      supplierId: new FormControl(serviceLogRawValue.supplierId),
    });
  }

  getServiceLog(form: ServiceLogFormGroup): IServiceLog | NewServiceLog {
    return this.convertServiceLogRawValueToServiceLog(form.getRawValue() as ServiceLogFormRawValue | NewServiceLogFormRawValue);
  }

  resetForm(form: ServiceLogFormGroup, serviceLog: ServiceLogFormGroupInput): void {
    const serviceLogRawValue = this.convertServiceLogToServiceLogRawValue({ ...this.getFormDefaults(), ...serviceLog });
    form.reset(
      {
        ...serviceLogRawValue,
        id: { value: serviceLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ServiceLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      serviceDate: currentTime,
    };
  }

  private convertServiceLogRawValueToServiceLog(
    rawServiceLog: ServiceLogFormRawValue | NewServiceLogFormRawValue,
  ): IServiceLog | NewServiceLog {
    return {
      ...rawServiceLog,
      serviceDate: dayjs(rawServiceLog.serviceDate, DATE_TIME_FORMAT),
    };
  }

  private convertServiceLogToServiceLogRawValue(
    serviceLog: IServiceLog | (Partial<NewServiceLog> & ServiceLogFormDefaults),
  ): ServiceLogFormRawValue | PartialWithRequiredKeyOf<NewServiceLogFormRawValue> {
    return {
      ...serviceLog,
      serviceDate: serviceLog.serviceDate ? serviceLog.serviceDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
