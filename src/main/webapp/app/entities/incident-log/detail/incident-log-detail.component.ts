import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IIncidentLog } from '../incident-log.model';

@Component({
  selector: 'jhi-incident-log-detail',
  templateUrl: './incident-log-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class IncidentLogDetailComponent {
  incidentLog = input<IIncidentLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
