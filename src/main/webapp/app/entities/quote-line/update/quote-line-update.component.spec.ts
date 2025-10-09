import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { QuoteLineService } from '../service/quote-line.service';
import { IQuoteLine } from '../quote-line.model';
import { QuoteLineFormService } from './quote-line-form.service';

import { QuoteLineUpdateComponent } from './quote-line-update.component';

describe('QuoteLine Management Update Component', () => {
  let comp: QuoteLineUpdateComponent;
  let fixture: ComponentFixture<QuoteLineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quoteLineFormService: QuoteLineFormService;
  let quoteLineService: QuoteLineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [QuoteLineUpdateComponent],
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
      .overrideTemplate(QuoteLineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuoteLineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quoteLineFormService = TestBed.inject(QuoteLineFormService);
    quoteLineService = TestBed.inject(QuoteLineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const quoteLine: IQuoteLine = { id: 3248 };

      activatedRoute.data = of({ quoteLine });
      comp.ngOnInit();

      expect(comp.quoteLine).toEqual(quoteLine);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuoteLine>>();
      const quoteLine = { id: 18028 };
      jest.spyOn(quoteLineFormService, 'getQuoteLine').mockReturnValue(quoteLine);
      jest.spyOn(quoteLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quoteLine }));
      saveSubject.complete();

      // THEN
      expect(quoteLineFormService.getQuoteLine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quoteLineService.update).toHaveBeenCalledWith(expect.objectContaining(quoteLine));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuoteLine>>();
      const quoteLine = { id: 18028 };
      jest.spyOn(quoteLineFormService, 'getQuoteLine').mockReturnValue({ id: null });
      jest.spyOn(quoteLineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteLine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quoteLine }));
      saveSubject.complete();

      // THEN
      expect(quoteLineFormService.getQuoteLine).toHaveBeenCalled();
      expect(quoteLineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuoteLine>>();
      const quoteLine = { id: 18028 };
      jest.spyOn(quoteLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quoteLineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
