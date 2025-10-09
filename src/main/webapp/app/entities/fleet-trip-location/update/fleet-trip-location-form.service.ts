import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFleetTripLocation, NewFleetTripLocation } from '../fleet-trip-location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFleetTripLocation for edit and NewFleetTripLocationFormGroupInput for create.
 */
type FleetTripLocationFormGroupInput = IFleetTripLocation | PartialWithRequiredKeyOf<NewFleetTripLocation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFleetTripLocation | NewFleetTripLocation> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type FleetTripLocationFormRawValue = FormValueOf<IFleetTripLocation>;

type NewFleetTripLocationFormRawValue = FormValueOf<NewFleetTripLocation>;

type FleetTripLocationFormDefaults = Pick<NewFleetTripLocation, 'id' | 'timestamp'>;

type FleetTripLocationFormGroupContent = {
  id: FormControl<FleetTripLocationFormRawValue['id'] | NewFleetTripLocation['id']>;
  fleetTripId: FormControl<FleetTripLocationFormRawValue['fleetTripId']>;
  timestamp: FormControl<FleetTripLocationFormRawValue['timestamp']>;
  latitude: FormControl<FleetTripLocationFormRawValue['latitude']>;
  longitude: FormControl<FleetTripLocationFormRawValue['longitude']>;
  speed: FormControl<FleetTripLocationFormRawValue['speed']>;
  heading: FormControl<FleetTripLocationFormRawValue['heading']>;
};

export type FleetTripLocationFormGroup = FormGroup<FleetTripLocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FleetTripLocationFormService {
  createFleetTripLocationFormGroup(fleetTripLocation: FleetTripLocationFormGroupInput = { id: null }): FleetTripLocationFormGroup {
    const fleetTripLocationRawValue = this.convertFleetTripLocationToFleetTripLocationRawValue({
      ...this.getFormDefaults(),
      ...fleetTripLocation,
    });
    return new FormGroup<FleetTripLocationFormGroupContent>({
      id: new FormControl(
        { value: fleetTripLocationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fleetTripId: new FormControl(fleetTripLocationRawValue.fleetTripId, {
        validators: [Validators.required],
      }),
      timestamp: new FormControl(fleetTripLocationRawValue.timestamp, {
        validators: [Validators.required],
      }),
      latitude: new FormControl(fleetTripLocationRawValue.latitude, {
        validators: [Validators.required],
      }),
      longitude: new FormControl(fleetTripLocationRawValue.longitude, {
        validators: [Validators.required],
      }),
      speed: new FormControl(fleetTripLocationRawValue.speed),
      heading: new FormControl(fleetTripLocationRawValue.heading),
    });
  }

  getFleetTripLocation(form: FleetTripLocationFormGroup): IFleetTripLocation | NewFleetTripLocation {
    return this.convertFleetTripLocationRawValueToFleetTripLocation(
      form.getRawValue() as FleetTripLocationFormRawValue | NewFleetTripLocationFormRawValue,
    );
  }

  resetForm(form: FleetTripLocationFormGroup, fleetTripLocation: FleetTripLocationFormGroupInput): void {
    const fleetTripLocationRawValue = this.convertFleetTripLocationToFleetTripLocationRawValue({
      ...this.getFormDefaults(),
      ...fleetTripLocation,
    });
    form.reset(
      {
        ...fleetTripLocationRawValue,
        id: { value: fleetTripLocationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FleetTripLocationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertFleetTripLocationRawValueToFleetTripLocation(
    rawFleetTripLocation: FleetTripLocationFormRawValue | NewFleetTripLocationFormRawValue,
  ): IFleetTripLocation | NewFleetTripLocation {
    return {
      ...rawFleetTripLocation,
      timestamp: dayjs(rawFleetTripLocation.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertFleetTripLocationToFleetTripLocationRawValue(
    fleetTripLocation: IFleetTripLocation | (Partial<NewFleetTripLocation> & FleetTripLocationFormDefaults),
  ): FleetTripLocationFormRawValue | PartialWithRequiredKeyOf<NewFleetTripLocationFormRawValue> {
    return {
      ...fleetTripLocation,
      timestamp: fleetTripLocation.timestamp ? fleetTripLocation.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
