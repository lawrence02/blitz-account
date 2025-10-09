import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuoteLineDetailComponent } from './quote-line-detail.component';

describe('QuoteLine Management Detail Component', () => {
  let comp: QuoteLineDetailComponent;
  let fixture: ComponentFixture<QuoteLineDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuoteLineDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./quote-line-detail.component').then(m => m.QuoteLineDetailComponent),
              resolve: { quoteLine: () => of({ id: 18028 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(QuoteLineDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QuoteLineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load quoteLine on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QuoteLineDetailComponent);

      // THEN
      expect(instance.quoteLine()).toEqual(expect.objectContaining({ id: 18028 }));
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
