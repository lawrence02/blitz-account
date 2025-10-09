import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ServiceLogService } from '../service/service-log.service';
import { IServiceLog } from '../service-log.model';
import { ServiceLogFormService } from './service-log-form.service';

import { ServiceLogUpdateComponent } from './service-log-update.component';

describe('ServiceLog Management Update Component', () => {
  let comp: ServiceLogUpdateComponent;
  let fixture: ComponentFixture<ServiceLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceLogFormService: ServiceLogFormService;
  let serviceLogService: ServiceLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ServiceLogUpdateComponent],
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
      .overrideTemplate(ServiceLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceLogFormService = TestBed.inject(ServiceLogFormService);
    serviceLogService = TestBed.inject(ServiceLogService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const serviceLog: IServiceLog = { id: 23397 };

      activatedRoute.data = of({ serviceLog });
      comp.ngOnInit();

      expect(comp.serviceLog).toEqual(serviceLog);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceLog>>();
      const serviceLog = { id: 2909 };
      jest.spyOn(serviceLogFormService, 'getServiceLog').mockReturnValue(serviceLog);
      jest.spyOn(serviceLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceLog }));
      saveSubject.complete();

      // THEN
      expect(serviceLogFormService.getServiceLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceLogService.update).toHaveBeenCalledWith(expect.objectContaining(serviceLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceLog>>();
      const serviceLog = { id: 2909 };
      jest.spyOn(serviceLogFormService, 'getServiceLog').mockReturnValue({ id: null });
      jest.spyOn(serviceLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceLog }));
      saveSubject.complete();

      // THEN
      expect(serviceLogFormService.getServiceLog).toHaveBeenCalled();
      expect(serviceLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceLog>>();
      const serviceLog = { id: 2909 };
      jest.spyOn(serviceLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
