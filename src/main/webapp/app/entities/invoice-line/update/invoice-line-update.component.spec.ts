import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { InvoiceLineService } from '../service/invoice-line.service';
import { IInvoiceLine } from '../invoice-line.model';
import { InvoiceLineFormService } from './invoice-line-form.service';

import { InvoiceLineUpdateComponent } from './invoice-line-update.component';

describe('InvoiceLine Management Update Component', () => {
  let comp: InvoiceLineUpdateComponent;
  let fixture: ComponentFixture<InvoiceLineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceLineFormService: InvoiceLineFormService;
  let invoiceLineService: InvoiceLineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InvoiceLineUpdateComponent],
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
      .overrideTemplate(InvoiceLineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceLineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceLineFormService = TestBed.inject(InvoiceLineFormService);
    invoiceLineService = TestBed.inject(InvoiceLineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const invoiceLine: IInvoiceLine = { id: 7582 };

      activatedRoute.data = of({ invoiceLine });
      comp.ngOnInit();

      expect(comp.invoiceLine).toEqual(invoiceLine);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceLine>>();
      const invoiceLine = { id: 8710 };
      jest.spyOn(invoiceLineFormService, 'getInvoiceLine').mockReturnValue(invoiceLine);
      jest.spyOn(invoiceLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceLine }));
      saveSubject.complete();

      // THEN
      expect(invoiceLineFormService.getInvoiceLine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceLineService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceLine));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceLine>>();
      const invoiceLine = { id: 8710 };
      jest.spyOn(invoiceLineFormService, 'getInvoiceLine').mockReturnValue({ id: null });
      jest.spyOn(invoiceLineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceLine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceLine }));
      saveSubject.complete();

      // THEN
      expect(invoiceLineFormService.getInvoiceLine).toHaveBeenCalled();
      expect(invoiceLineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceLine>>();
      const invoiceLine = { id: 8710 };
      jest.spyOn(invoiceLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceLineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
