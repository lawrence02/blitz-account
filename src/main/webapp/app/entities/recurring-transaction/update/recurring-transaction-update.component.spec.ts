import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { RecurringTransactionService } from '../service/recurring-transaction.service';
import { IRecurringTransaction } from '../recurring-transaction.model';
import { RecurringTransactionFormService } from './recurring-transaction-form.service';

import { RecurringTransactionUpdateComponent } from './recurring-transaction-update.component';

describe('RecurringTransaction Management Update Component', () => {
  let comp: RecurringTransactionUpdateComponent;
  let fixture: ComponentFixture<RecurringTransactionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recurringTransactionFormService: RecurringTransactionFormService;
  let recurringTransactionService: RecurringTransactionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RecurringTransactionUpdateComponent],
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
      .overrideTemplate(RecurringTransactionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecurringTransactionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recurringTransactionFormService = TestBed.inject(RecurringTransactionFormService);
    recurringTransactionService = TestBed.inject(RecurringTransactionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const recurringTransaction: IRecurringTransaction = { id: 10255 };

      activatedRoute.data = of({ recurringTransaction });
      comp.ngOnInit();

      expect(comp.recurringTransaction).toEqual(recurringTransaction);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecurringTransaction>>();
      const recurringTransaction = { id: 24480 };
      jest.spyOn(recurringTransactionFormService, 'getRecurringTransaction').mockReturnValue(recurringTransaction);
      jest.spyOn(recurringTransactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recurringTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recurringTransaction }));
      saveSubject.complete();

      // THEN
      expect(recurringTransactionFormService.getRecurringTransaction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recurringTransactionService.update).toHaveBeenCalledWith(expect.objectContaining(recurringTransaction));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecurringTransaction>>();
      const recurringTransaction = { id: 24480 };
      jest.spyOn(recurringTransactionFormService, 'getRecurringTransaction').mockReturnValue({ id: null });
      jest.spyOn(recurringTransactionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recurringTransaction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recurringTransaction }));
      saveSubject.complete();

      // THEN
      expect(recurringTransactionFormService.getRecurringTransaction).toHaveBeenCalled();
      expect(recurringTransactionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecurringTransaction>>();
      const recurringTransaction = { id: 24480 };
      jest.spyOn(recurringTransactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recurringTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recurringTransactionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
