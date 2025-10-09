import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { IncidentLogDetailComponent } from './incident-log-detail.component';

describe('IncidentLog Management Detail Component', () => {
  let comp: IncidentLogDetailComponent;
  let fixture: ComponentFixture<IncidentLogDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncidentLogDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./incident-log-detail.component').then(m => m.IncidentLogDetailComponent),
              resolve: { incidentLog: () => of({ id: 5606 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(IncidentLogDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IncidentLogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load incidentLog on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', IncidentLogDetailComponent);

      // THEN
      expect(instance.incidentLog()).toEqual(expect.objectContaining({ id: 5606 }));
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
