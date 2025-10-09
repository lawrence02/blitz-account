import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { FleetTripLocationService } from '../service/fleet-trip-location.service';
import { IFleetTripLocation } from '../fleet-trip-location.model';
import { FleetTripLocationFormService } from './fleet-trip-location-form.service';

import { FleetTripLocationUpdateComponent } from './fleet-trip-location-update.component';

describe('FleetTripLocation Management Update Component', () => {
  let comp: FleetTripLocationUpdateComponent;
  let fixture: ComponentFixture<FleetTripLocationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fleetTripLocationFormService: FleetTripLocationFormService;
  let fleetTripLocationService: FleetTripLocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FleetTripLocationUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FleetTripLocationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FleetTripLocationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fleetTripLocationFormService = TestBed.inject(FleetTripLocationFormService);
    fleetTripLocationService = TestBed.inject(FleetTripLocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fleetTripLocation: IFleetTripLocation = { id: 23545 };

      activatedRoute.data = of({ fleetTripLocation });
      comp.ngOnInit();

      expect(comp.fleetTripLocation).toEqual(fleetTripLocation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFleetTripLocation>>();
      const fleetTripLocation = { id: 24349 };
      jest.spyOn(fleetTripLocationFormService, 'getFleetTripLocation').mockReturnValue(fleetTripLocation);
      jest.spyOn(fleetTripLocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fleetTripLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fleetTripLocation }));
      saveSubject.complete();

      // THEN
      expect(fleetTripLocationFormService.getFleetTripLocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fleetTripLocationService.update).toHaveBeenCalledWith(expect.objectContaining(fleetTripLocation));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFleetTripLocation>>();
      const fleetTripLocation = { id: 24349 };
      jest.spyOn(fleetTripLocationFormService, 'getFleetTripLocation').mockReturnValue({ id: null });
      jest.spyOn(fleetTripLocationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fleetTripLocation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fleetTripLocation }));
      saveSubject.complete();

      // THEN
      expect(fleetTripLocationFormService.getFleetTripLocation).toHaveBeenCalled();
      expect(fleetTripLocationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFleetTripLocation>>();
      const fleetTripLocation = { id: 24349 };
      jest.spyOn(fleetTripLocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fleetTripLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fleetTripLocationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
