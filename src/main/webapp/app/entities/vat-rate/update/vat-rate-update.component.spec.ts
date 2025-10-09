import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { VATRateService } from '../service/vat-rate.service';
import { IVATRate } from '../vat-rate.model';
import { VATRateFormService } from './vat-rate-form.service';

import { VATRateUpdateComponent } from './vat-rate-update.component';

describe('VATRate Management Update Component', () => {
  let comp: VATRateUpdateComponent;
  let fixture: ComponentFixture<VATRateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vATRateFormService: VATRateFormService;
  let vATRateService: VATRateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VATRateUpdateComponent],
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
      .overrideTemplate(VATRateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VATRateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vATRateFormService = TestBed.inject(VATRateFormService);
    vATRateService = TestBed.inject(VATRateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const vATRate: IVATRate = { id: 11155 };

      activatedRoute.data = of({ vATRate });
      comp.ngOnInit();

      expect(comp.vATRate).toEqual(vATRate);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVATRate>>();
      const vATRate = { id: 25659 };
      jest.spyOn(vATRateFormService, 'getVATRate').mockReturnValue(vATRate);
      jest.spyOn(vATRateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vATRate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vATRate }));
      saveSubject.complete();

      // THEN
      expect(vATRateFormService.getVATRate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vATRateService.update).toHaveBeenCalledWith(expect.objectContaining(vATRate));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVATRate>>();
      const vATRate = { id: 25659 };
      jest.spyOn(vATRateFormService, 'getVATRate').mockReturnValue({ id: null });
      jest.spyOn(vATRateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vATRate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vATRate }));
      saveSubject.complete();

      // THEN
      expect(vATRateFormService.getVATRate).toHaveBeenCalled();
      expect(vATRateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVATRate>>();
      const vATRate = { id: 25659 };
      jest.spyOn(vATRateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vATRate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vATRateService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
