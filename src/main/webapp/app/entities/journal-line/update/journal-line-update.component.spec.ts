import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { JournalLineService } from '../service/journal-line.service';
import { IJournalLine } from '../journal-line.model';
import { JournalLineFormService } from './journal-line-form.service';

import { JournalLineUpdateComponent } from './journal-line-update.component';

describe('JournalLine Management Update Component', () => {
  let comp: JournalLineUpdateComponent;
  let fixture: ComponentFixture<JournalLineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let journalLineFormService: JournalLineFormService;
  let journalLineService: JournalLineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [JournalLineUpdateComponent],
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
      .overrideTemplate(JournalLineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JournalLineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    journalLineFormService = TestBed.inject(JournalLineFormService);
    journalLineService = TestBed.inject(JournalLineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const journalLine: IJournalLine = { id: 32513 };

      activatedRoute.data = of({ journalLine });
      comp.ngOnInit();

      expect(comp.journalLine).toEqual(journalLine);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJournalLine>>();
      const journalLine = { id: 13943 };
      jest.spyOn(journalLineFormService, 'getJournalLine').mockReturnValue(journalLine);
      jest.spyOn(journalLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ journalLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: journalLine }));
      saveSubject.complete();

      // THEN
      expect(journalLineFormService.getJournalLine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(journalLineService.update).toHaveBeenCalledWith(expect.objectContaining(journalLine));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJournalLine>>();
      const journalLine = { id: 13943 };
      jest.spyOn(journalLineFormService, 'getJournalLine').mockReturnValue({ id: null });
      jest.spyOn(journalLineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ journalLine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: journalLine }));
      saveSubject.complete();

      // THEN
      expect(journalLineFormService.getJournalLine).toHaveBeenCalled();
      expect(journalLineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJournalLine>>();
      const journalLine = { id: 13943 };
      jest.spyOn(journalLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ journalLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(journalLineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
