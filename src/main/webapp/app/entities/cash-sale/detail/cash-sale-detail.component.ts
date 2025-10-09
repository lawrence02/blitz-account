import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ICashSale } from '../cash-sale.model';

@Component({
  selector: 'jhi-cash-sale-detail',
  templateUrl: './cash-sale-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class CashSaleDetailComponent {
  cashSale = input<ICashSale | null>(null);

  previousState(): void {
    window.history.back();
  }
}
