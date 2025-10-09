import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CashSaleService } from '../service/cash-sale.service';
import { ICashSale } from '../cash-sale.model';
import { CashSaleFormService } from './cash-sale-form.service';

import { CashSaleUpdateComponent } from './cash-sale-update.component';

describe('CashSale Management Update Component', () => {
  let comp: CashSaleUpdateComponent;
  let fixture: ComponentFixture<CashSaleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cashSaleFormService: CashSaleFormService;
  let cashSaleService: CashSaleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CashSaleUpdateComponent],
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
      .overrideTemplate(CashSaleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CashSaleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cashSaleFormService = TestBed.inject(CashSaleFormService);
    cashSaleService = TestBed.inject(CashSaleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const cashSale: ICashSale = { id: 23860 };

      activatedRoute.data = of({ cashSale });
      comp.ngOnInit();

      expect(comp.cashSale).toEqual(cashSale);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICashSale>>();
      const cashSale = { id: 18922 };
      jest.spyOn(cashSaleFormService, 'getCashSale').mockReturnValue(cashSale);
      jest.spyOn(cashSaleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashSale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cashSale }));
      saveSubject.complete();

      // THEN
      expect(cashSaleFormService.getCashSale).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cashSaleService.update).toHaveBeenCalledWith(expect.objectContaining(cashSale));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICashSale>>();
      const cashSale = { id: 18922 };
      jest.spyOn(cashSaleFormService, 'getCashSale').mockReturnValue({ id: null });
      jest.spyOn(cashSaleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashSale: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cashSale }));
      saveSubject.complete();

      // THEN
      expect(cashSaleFormService.getCashSale).toHaveBeenCalled();
      expect(cashSaleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICashSale>>();
      const cashSale = { id: 18922 };
      jest.spyOn(cashSaleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashSale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cashSaleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
