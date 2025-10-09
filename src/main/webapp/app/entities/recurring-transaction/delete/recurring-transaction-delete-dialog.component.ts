import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRecurringTransaction } from '../recurring-transaction.model';
import { RecurringTransactionService } from '../service/recurring-transaction.service';

@Component({
  templateUrl: './recurring-transaction-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RecurringTransactionDeleteDialogComponent {
  recurringTransaction?: IRecurringTransaction;

  protected recurringTransactionService = inject(RecurringTransactionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recurringTransactionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
