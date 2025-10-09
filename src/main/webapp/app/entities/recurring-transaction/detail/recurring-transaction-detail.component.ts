import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IRecurringTransaction } from '../recurring-transaction.model';

@Component({
  selector: 'jhi-recurring-transaction-detail',
  templateUrl: './recurring-transaction-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class RecurringTransactionDetailComponent {
  recurringTransaction = input<IRecurringTransaction | null>(null);

  previousState(): void {
    window.history.back();
  }
}
