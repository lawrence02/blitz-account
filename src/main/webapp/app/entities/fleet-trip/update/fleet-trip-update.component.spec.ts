import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { FleetTripService } from '../service/fleet-trip.service';
import { IFleetTrip } from '../fleet-trip.model';
import { FleetTripFormService } from './fleet-trip-form.service';

import { FleetTripUpdateComponent } from './fleet-trip-update.component';

describe('FleetTrip Management Update Component', () => {
  let comp: FleetTripUpdateComponent;
  let fixture: ComponentFixture<FleetTripUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fleetTripFormService: FleetTripFormService;
  let fleetTripService: FleetTripService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FleetTripUpdateComponent],
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
      .overrideTemplate(FleetTripUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FleetTripUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fleetTripFormService = TestBed.inject(FleetTripFormService);
    fleetTripService = TestBed.inject(FleetTripService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fleetTrip: IFleetTrip = { id: 30736 };

      activatedRoute.data = of({ fleetTrip });
      comp.ngOnInit();

      expect(comp.fleetTrip).toEqual(fleetTrip);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFleetTrip>>();
      const fleetTrip = { id: 2208 };
      jest.spyOn(fleetTripFormService, 'getFleetTrip').mockReturnValue(fleetTrip);
      jest.spyOn(fleetTripService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fleetTrip });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fleetTrip }));
      saveSubject.complete();

      // THEN
      expect(fleetTripFormService.getFleetTrip).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fleetTripService.update).toHaveBeenCalledWith(expect.objectContaining(fleetTrip));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFleetTrip>>();
      const fleetTrip = { id: 2208 };
      jest.spyOn(fleetTripFormService, 'getFleetTrip').mockReturnValue({ id: null });
      jest.spyOn(fleetTripService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fleetTrip: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fleetTrip }));
      saveSubject.complete();

      // THEN
      expect(fleetTripFormService.getFleetTrip).toHaveBeenCalled();
      expect(fleetTripService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFleetTrip>>();
      const fleetTrip = { id: 2208 };
      jest.spyOn(fleetTripService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fleetTrip });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fleetTripService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
