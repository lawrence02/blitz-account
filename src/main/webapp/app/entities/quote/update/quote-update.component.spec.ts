import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { QuoteService } from '../service/quote.service';
import { IQuote } from '../quote.model';
import { QuoteFormService } from './quote-form.service';

import { QuoteUpdateComponent } from './quote-update.component';

describe('Quote Management Update Component', () => {
  let comp: QuoteUpdateComponent;
  let fixture: ComponentFixture<QuoteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quoteFormService: QuoteFormService;
  let quoteService: QuoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [QuoteUpdateComponent],
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
      .overrideTemplate(QuoteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuoteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quoteFormService = TestBed.inject(QuoteFormService);
    quoteService = TestBed.inject(QuoteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const quote: IQuote = { id: 23928 };

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(comp.quote).toEqual(quote);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuote>>();
      const quote = { id: 10209 };
      jest.spyOn(quoteFormService, 'getQuote').mockReturnValue(quote);
      jest.spyOn(quoteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quote }));
      saveSubject.complete();

      // THEN
      expect(quoteFormService.getQuote).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quoteService.update).toHaveBeenCalledWith(expect.objectContaining(quote));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuote>>();
      const quote = { id: 10209 };
      jest.spyOn(quoteFormService, 'getQuote').mockReturnValue({ id: null });
      jest.spyOn(quoteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quote: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quote }));
      saveSubject.complete();

      // THEN
      expect(quoteFormService.getQuote).toHaveBeenCalled();
      expect(quoteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuote>>();
      const quote = { id: 10209 };
      jest.spyOn(quoteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quoteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
