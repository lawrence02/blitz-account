import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IChartOfAccount } from '../chart-of-account.model';

@Component({
  selector: 'jhi-chart-of-account-detail',
  templateUrl: './chart-of-account-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ChartOfAccountDetailComponent {
  chartOfAccount = input<IChartOfAccount | null>(null);

  previousState(): void {
    window.history.back();
  }
}
