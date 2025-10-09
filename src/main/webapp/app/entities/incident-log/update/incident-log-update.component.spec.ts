import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IncidentLogService } from '../service/incident-log.service';
import { IIncidentLog } from '../incident-log.model';
import { IncidentLogFormService } from './incident-log-form.service';

import { IncidentLogUpdateComponent } from './incident-log-update.component';

describe('IncidentLog Management Update Component', () => {
  let comp: IncidentLogUpdateComponent;
  let fixture: ComponentFixture<IncidentLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let incidentLogFormService: IncidentLogFormService;
  let incidentLogService: IncidentLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [IncidentLogUpdateComponent],
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
      .overrideTemplate(IncidentLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncidentLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    incidentLogFormService = TestBed.inject(IncidentLogFormService);
    incidentLogService = TestBed.inject(IncidentLogService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const incidentLog: IIncidentLog = { id: 593 };

      activatedRoute.data = of({ incidentLog });
      comp.ngOnInit();

      expect(comp.incidentLog).toEqual(incidentLog);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncidentLog>>();
      const incidentLog = { id: 5606 };
      jest.spyOn(incidentLogFormService, 'getIncidentLog').mockReturnValue(incidentLog);
      jest.spyOn(incidentLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incidentLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incidentLog }));
      saveSubject.complete();

      // THEN
      expect(incidentLogFormService.getIncidentLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(incidentLogService.update).toHaveBeenCalledWith(expect.objectContaining(incidentLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncidentLog>>();
      const incidentLog = { id: 5606 };
      jest.spyOn(incidentLogFormService, 'getIncidentLog').mockReturnValue({ id: null });
      jest.spyOn(incidentLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incidentLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incidentLog }));
      saveSubject.complete();

      // THEN
      expect(incidentLogFormService.getIncidentLog).toHaveBeenCalled();
      expect(incidentLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncidentLog>>();
      const incidentLog = { id: 5606 };
      jest.spyOn(incidentLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incidentLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(incidentLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
