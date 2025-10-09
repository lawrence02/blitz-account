import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { FuelLogService } from '../service/fuel-log.service';
import { IFuelLog } from '../fuel-log.model';
import { FuelLogFormService } from './fuel-log-form.service';

import { FuelLogUpdateComponent } from './fuel-log-update.component';

describe('FuelLog Management Update Component', () => {
  let comp: FuelLogUpdateComponent;
  let fixture: ComponentFixture<FuelLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fuelLogFormService: FuelLogFormService;
  let fuelLogService: FuelLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FuelLogUpdateComponent],
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
      .overrideTemplate(FuelLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FuelLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fuelLogFormService = TestBed.inject(FuelLogFormService);
    fuelLogService = TestBed.inject(FuelLogService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fuelLog: IFuelLog = { id: 10395 };

      activatedRoute.data = of({ fuelLog });
      comp.ngOnInit();

      expect(comp.fuelLog).toEqual(fuelLog);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuelLog>>();
      const fuelLog = { id: 32387 };
      jest.spyOn(fuelLogFormService, 'getFuelLog').mockReturnValue(fuelLog);
      jest.spyOn(fuelLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fuelLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fuelLog }));
      saveSubject.complete();

      // THEN
      expect(fuelLogFormService.getFuelLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fuelLogService.update).toHaveBeenCalledWith(expect.objectContaining(fuelLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuelLog>>();
      const fuelLog = { id: 32387 };
      jest.spyOn(fuelLogFormService, 'getFuelLog').mockReturnValue({ id: null });
      jest.spyOn(fuelLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fuelLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fuelLog }));
      saveSubject.complete();

      // THEN
      expect(fuelLogFormService.getFuelLog).toHaveBeenCalled();
      expect(fuelLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuelLog>>();
      const fuelLog = { id: 32387 };
      jest.spyOn(fuelLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fuelLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fuelLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
