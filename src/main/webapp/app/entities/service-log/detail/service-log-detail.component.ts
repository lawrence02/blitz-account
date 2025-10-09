import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IServiceLog } from '../service-log.model';

@Component({
  selector: 'jhi-service-log-detail',
  templateUrl: './service-log-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ServiceLogDetailComponent {
  serviceLog = input<IServiceLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
