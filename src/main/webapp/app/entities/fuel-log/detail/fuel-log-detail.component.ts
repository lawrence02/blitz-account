import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IFuelLog } from '../fuel-log.model';

@Component({
  selector: 'jhi-fuel-log-detail',
  templateUrl: './fuel-log-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class FuelLogDetailComponent {
  fuelLog = input<IFuelLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
