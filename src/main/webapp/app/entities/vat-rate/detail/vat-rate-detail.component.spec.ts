import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VATRateDetailComponent } from './vat-rate-detail.component';

describe('VATRate Management Detail Component', () => {
  let comp: VATRateDetailComponent;
  let fixture: ComponentFixture<VATRateDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VATRateDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./vat-rate-detail.component').then(m => m.VATRateDetailComponent),
              resolve: { vATRate: () => of({ id: 25659 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VATRateDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VATRateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load vATRate on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VATRateDetailComponent);

      // THEN
      expect(instance.vATRate()).toEqual(expect.objectContaining({ id: 25659 }));
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
