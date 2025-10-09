import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { JournalService } from '../service/journal.service';
import { IJournal } from '../journal.model';
import { JournalFormService } from './journal-form.service';

import { JournalUpdateComponent } from './journal-update.component';

describe('Journal Management Update Component', () => {
  let comp: JournalUpdateComponent;
  let fixture: ComponentFixture<JournalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let journalFormService: JournalFormService;
  let journalService: JournalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [JournalUpdateComponent],
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
      .overrideTemplate(JournalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JournalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    journalFormService = TestBed.inject(JournalFormService);
    journalService = TestBed.inject(JournalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const journal: IJournal = { id: 5439 };

      activatedRoute.data = of({ journal });
      comp.ngOnInit();

      expect(comp.journal).toEqual(journal);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJournal>>();
      const journal = { id: 21599 };
      jest.spyOn(journalFormService, 'getJournal').mockReturnValue(journal);
      jest.spyOn(journalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ journal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: journal }));
      saveSubject.complete();

      // THEN
      expect(journalFormService.getJournal).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(journalService.update).toHaveBeenCalledWith(expect.objectContaining(journal));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJournal>>();
      const journal = { id: 21599 };
      jest.spyOn(journalFormService, 'getJournal').mockReturnValue({ id: null });
      jest.spyOn(journalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ journal: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: journal }));
      saveSubject.complete();

      // THEN
      expect(journalFormService.getJournal).toHaveBeenCalled();
      expect(journalService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJournal>>();
      const journal = { id: 21599 };
      jest.spyOn(journalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ journal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(journalService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
