import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IVATRate } from '../vat-rate.model';

@Component({
  selector: 'jhi-vat-rate-detail',
  templateUrl: './vat-rate-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class VATRateDetailComponent {
  vATRate = input<IVATRate | null>(null);

  previousState(): void {
    window.history.back();
  }
}
