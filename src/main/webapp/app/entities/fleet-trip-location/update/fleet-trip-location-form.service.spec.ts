import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fleet-trip-location.test-samples';

import { FleetTripLocationFormService } from './fleet-trip-location-form.service';

describe('FleetTripLocation Form Service', () => {
  let service: FleetTripLocationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FleetTripLocationFormService);
  });

  describe('Service methods', () => {
    describe('createFleetTripLocationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFleetTripLocationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fleetTripId: expect.any(Object),
            timestamp: expect.any(Object),
            latitude: expect.any(Object),
            longitude: expect.any(Object),
            speed: expect.any(Object),
            heading: expect.any(Object),
          }),
        );
      });

      it('passing IFleetTripLocation should create a new form with FormGroup', () => {
        const formGroup = service.createFleetTripLocationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fleetTripId: expect.any(Object),
            timestamp: expect.any(Object),
            latitude: expect.any(Object),
            longitude: expect.any(Object),
            speed: expect.any(Object),
            heading: expect.any(Object),
          }),
        );
      });
    });

    describe('getFleetTripLocation', () => {
      it('should return NewFleetTripLocation for default FleetTripLocation initial value', () => {
        const formGroup = service.createFleetTripLocationFormGroup(sampleWithNewData);

        const fleetTripLocation = service.getFleetTripLocation(formGroup) as any;

        expect(fleetTripLocation).toMatchObject(sampleWithNewData);
      });

      it('should return NewFleetTripLocation for empty FleetTripLocation initial value', () => {
        const formGroup = service.createFleetTripLocationFormGroup();

        const fleetTripLocation = service.getFleetTripLocation(formGroup) as any;

        expect(fleetTripLocation).toMatchObject({});
      });

      it('should return IFleetTripLocation', () => {
        const formGroup = service.createFleetTripLocationFormGroup(sampleWithRequiredData);

        const fleetTripLocation = service.getFleetTripLocation(formGroup) as any;

        expect(fleetTripLocation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFleetTripLocation should not enable id FormControl', () => {
        const formGroup = service.createFleetTripLocationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFleetTripLocation should disable id FormControl', () => {
        const formGroup = service.createFleetTripLocationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
