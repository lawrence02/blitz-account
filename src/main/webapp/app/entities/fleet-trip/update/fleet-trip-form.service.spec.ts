import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fleet-trip.test-samples';

import { FleetTripFormService } from './fleet-trip-form.service';

describe('FleetTrip Form Service', () => {
  let service: FleetTripFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FleetTripFormService);
  });

  describe('Service methods', () => {
    describe('createFleetTripFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFleetTripFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vehicleId: expect.any(Object),
            driverId: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            distanceKm: expect.any(Object),
            startLocation: expect.any(Object),
            endLocation: expect.any(Object),
            loadType: expect.any(Object),
            loadDescription: expect.any(Object),
            routeGeoCoordinates: expect.any(Object),
            tripCost: expect.any(Object),
          }),
        );
      });

      it('passing IFleetTrip should create a new form with FormGroup', () => {
        const formGroup = service.createFleetTripFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vehicleId: expect.any(Object),
            driverId: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            distanceKm: expect.any(Object),
            startLocation: expect.any(Object),
            endLocation: expect.any(Object),
            loadType: expect.any(Object),
            loadDescription: expect.any(Object),
            routeGeoCoordinates: expect.any(Object),
            tripCost: expect.any(Object),
          }),
        );
      });
    });

    describe('getFleetTrip', () => {
      it('should return NewFleetTrip for default FleetTrip initial value', () => {
        const formGroup = service.createFleetTripFormGroup(sampleWithNewData);

        const fleetTrip = service.getFleetTrip(formGroup) as any;

        expect(fleetTrip).toMatchObject(sampleWithNewData);
      });

      it('should return NewFleetTrip for empty FleetTrip initial value', () => {
        const formGroup = service.createFleetTripFormGroup();

        const fleetTrip = service.getFleetTrip(formGroup) as any;

        expect(fleetTrip).toMatchObject({});
      });

      it('should return IFleetTrip', () => {
        const formGroup = service.createFleetTripFormGroup(sampleWithRequiredData);

        const fleetTrip = service.getFleetTrip(formGroup) as any;

        expect(fleetTrip).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFleetTrip should not enable id FormControl', () => {
        const formGroup = service.createFleetTripFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFleetTrip should disable id FormControl', () => {
        const formGroup = service.createFleetTripFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
