import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IFleetTripLocation } from '../fleet-trip-location.model';

@Component({
  selector: 'jhi-fleet-trip-location-detail',
  templateUrl: './fleet-trip-location-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class FleetTripLocationDetailComponent {
  fleetTripLocation = input<IFleetTripLocation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
