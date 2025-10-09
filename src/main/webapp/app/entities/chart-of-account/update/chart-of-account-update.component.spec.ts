import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ChartOfAccountService } from '../service/chart-of-account.service';
import { IChartOfAccount } from '../chart-of-account.model';
import { ChartOfAccountFormService } from './chart-of-account-form.service';

import { ChartOfAccountUpdateComponent } from './chart-of-account-update.component';

describe('ChartOfAccount Management Update Component', () => {
  let comp: ChartOfAccountUpdateComponent;
  let fixture: ComponentFixture<ChartOfAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chartOfAccountFormService: ChartOfAccountFormService;
  let chartOfAccountService: ChartOfAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ChartOfAccountUpdateComponent],
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
      .overrideTemplate(ChartOfAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChartOfAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chartOfAccountFormService = TestBed.inject(ChartOfAccountFormService);
    chartOfAccountService = TestBed.inject(ChartOfAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const chartOfAccount: IChartOfAccount = { id: 20252 };

      activatedRoute.data = of({ chartOfAccount });
      comp.ngOnInit();

      expect(comp.chartOfAccount).toEqual(chartOfAccount);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChartOfAccount>>();
      const chartOfAccount = { id: 20785 };
      jest.spyOn(chartOfAccountFormService, 'getChartOfAccount').mockReturnValue(chartOfAccount);
      jest.spyOn(chartOfAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chartOfAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chartOfAccount }));
      saveSubject.complete();

      // THEN
      expect(chartOfAccountFormService.getChartOfAccount).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(chartOfAccountService.update).toHaveBeenCalledWith(expect.objectContaining(chartOfAccount));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChartOfAccount>>();
      const chartOfAccount = { id: 20785 };
      jest.spyOn(chartOfAccountFormService, 'getChartOfAccount').mockReturnValue({ id: null });
      jest.spyOn(chartOfAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chartOfAccount: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chartOfAccount }));
      saveSubject.complete();

      // THEN
      expect(chartOfAccountFormService.getChartOfAccount).toHaveBeenCalled();
      expect(chartOfAccountService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChartOfAccount>>();
      const chartOfAccount = { id: 20785 };
      jest.spyOn(chartOfAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chartOfAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(chartOfAccountService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
