import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ChartOfAccountDetailComponent } from './chart-of-account-detail.component';

describe('ChartOfAccount Management Detail Component', () => {
  let comp: ChartOfAccountDetailComponent;
  let fixture: ComponentFixture<ChartOfAccountDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChartOfAccountDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./chart-of-account-detail.component').then(m => m.ChartOfAccountDetailComponent),
              resolve: { chartOfAccount: () => of({ id: 20785 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ChartOfAccountDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartOfAccountDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load chartOfAccount on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ChartOfAccountDetailComponent);

      // THEN
      expect(instance.chartOfAccount()).toEqual(expect.objectContaining({ id: 20785 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
