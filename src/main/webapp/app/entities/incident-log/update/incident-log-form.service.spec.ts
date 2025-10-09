import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../incident-log.test-samples';

import { IncidentLogFormService } from './incident-log-form.service';

describe('IncidentLog Form Service', () => {
  let service: IncidentLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IncidentLogFormService);
  });

  describe('Service methods', () => {
    describe('createIncidentLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIncidentLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vehicleId: expect.any(Object),
            tripId: expect.any(Object),
            incidentDate: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            cost: expect.any(Object),
          }),
        );
      });

      it('passing IIncidentLog should create a new form with FormGroup', () => {
        const formGroup = service.createIncidentLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vehicleId: expect.any(Object),
            tripId: expect.any(Object),
            incidentDate: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            cost: expect.any(Object),
          }),
        );
      });
    });

    describe('getIncidentLog', () => {
      it('should return NewIncidentLog for default IncidentLog initial value', () => {
        const formGroup = service.createIncidentLogFormGroup(sampleWithNewData);

        const incidentLog = service.getIncidentLog(formGroup) as any;

        expect(incidentLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewIncidentLog for empty IncidentLog initial value', () => {
        const formGroup = service.createIncidentLogFormGroup();

        const incidentLog = service.getIncidentLog(formGroup) as any;

        expect(incidentLog).toMatchObject({});
      });

      it('should return IIncidentLog', () => {
        const formGroup = service.createIncidentLogFormGroup(sampleWithRequiredData);

        const incidentLog = service.getIncidentLog(formGroup) as any;

        expect(incidentLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIncidentLog should not enable id FormControl', () => {
        const formGroup = service.createIncidentLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIncidentLog should disable id FormControl', () => {
        const formGroup = service.createIncidentLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
