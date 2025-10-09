import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICurrency } from '../currency.model';

@Component({
  selector: 'jhi-currency-detail',
  templateUrl: './currency-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CurrencyDetailComponent {
  currency = input<ICurrency | null>(null);

  previousState(): void {
    window.history.back();
  }
}
