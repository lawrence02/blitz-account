import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FleetTripLocationDetailComponent } from './fleet-trip-location-detail.component';

describe('FleetTripLocation Management Detail Component', () => {
  let comp: FleetTripLocationDetailComponent;
  let fixture: ComponentFixture<FleetTripLocationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FleetTripLocationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./fleet-trip-location-detail.component').then(m => m.FleetTripLocationDetailComponent),
              resolve: { fleetTripLocation: () => of({ id: 24349 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FleetTripLocationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FleetTripLocationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load fleetTripLocation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FleetTripLocationDetailComponent);

      // THEN
      expect(instance.fleetTripLocation()).toEqual(expect.objectContaining({ id: 24349 }));
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
