import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFleetTrip, NewFleetTrip } from '../fleet-trip.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFleetTrip for edit and NewFleetTripFormGroupInput for create.
 */
type FleetTripFormGroupInput = IFleetTrip | PartialWithRequiredKeyOf<NewFleetTrip>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFleetTrip | NewFleetTrip> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type FleetTripFormRawValue = FormValueOf<IFleetTrip>;

type NewFleetTripFormRawValue = FormValueOf<NewFleetTrip>;

type FleetTripFormDefaults = Pick<NewFleetTrip, 'id' | 'startDate' | 'endDate'>;

type FleetTripFormGroupContent = {
  id: FormControl<FleetTripFormRawValue['id'] | NewFleetTrip['id']>;
  vehicleId: FormControl<FleetTripFormRawValue['vehicleId']>;
  driverId: FormControl<FleetTripFormRawValue['driverId']>;
  startDate: FormControl<FleetTripFormRawValue['startDate']>;
  endDate: FormControl<FleetTripFormRawValue['endDate']>;
  distanceKm: FormControl<FleetTripFormRawValue['distanceKm']>;
  startLocation: FormControl<FleetTripFormRawValue['startLocation']>;
  endLocation: FormControl<FleetTripFormRawValue['endLocation']>;
  loadType: FormControl<FleetTripFormRawValue['loadType']>;
  loadDescription: FormControl<FleetTripFormRawValue['loadDescription']>;
  routeGeoCoordinates: FormControl<FleetTripFormRawValue['routeGeoCoordinates']>;
  tripCost: FormControl<FleetTripFormRawValue['tripCost']>;
};

export type FleetTripFormGroup = FormGroup<FleetTripFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FleetTripFormService {
  createFleetTripFormGroup(fleetTrip: FleetTripFormGroupInput = { id: null }): FleetTripFormGroup {
    const fleetTripRawValue = this.convertFleetTripToFleetTripRawValue({
      ...this.getFormDefaults(),
      ...fleetTrip,
    });
    return new FormGroup<FleetTripFormGroupContent>({
      id: new FormControl(
        { value: fleetTripRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      vehicleId: new FormControl(fleetTripRawValue.vehicleId, {
        validators: [Validators.required],
      }),
      driverId: new FormControl(fleetTripRawValue.driverId),
      startDate: new FormControl(fleetTripRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(fleetTripRawValue.endDate),
      distanceKm: new FormControl(fleetTripRawValue.distanceKm),
      startLocation: new FormControl(fleetTripRawValue.startLocation),
      endLocation: new FormControl(fleetTripRawValue.endLocation),
      loadType: new FormControl(fleetTripRawValue.loadType),
      loadDescription: new FormControl(fleetTripRawValue.loadDescription),
      routeGeoCoordinates: new FormControl(fleetTripRawValue.routeGeoCoordinates),
      tripCost: new FormControl(fleetTripRawValue.tripCost),
    });
  }

  getFleetTrip(form: FleetTripFormGroup): IFleetTrip | NewFleetTrip {
    return this.convertFleetTripRawValueToFleetTrip(form.getRawValue() as FleetTripFormRawValue | NewFleetTripFormRawValue);
  }

  resetForm(form: FleetTripFormGroup, fleetTrip: FleetTripFormGroupInput): void {
    const fleetTripRawValue = this.convertFleetTripToFleetTripRawValue({ ...this.getFormDefaults(), ...fleetTrip });
    form.reset(
      {
        ...fleetTripRawValue,
        id: { value: fleetTripRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FleetTripFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertFleetTripRawValueToFleetTrip(rawFleetTrip: FleetTripFormRawValue | NewFleetTripFormRawValue): IFleetTrip | NewFleetTrip {
    return {
      ...rawFleetTrip,
      startDate: dayjs(rawFleetTrip.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawFleetTrip.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertFleetTripToFleetTripRawValue(
    fleetTrip: IFleetTrip | (Partial<NewFleetTrip> & FleetTripFormDefaults),
  ): FleetTripFormRawValue | PartialWithRequiredKeyOf<NewFleetTripFormRawValue> {
    return {
      ...fleetTrip,
      startDate: fleetTrip.startDate ? fleetTrip.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: fleetTrip.endDate ? fleetTrip.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
