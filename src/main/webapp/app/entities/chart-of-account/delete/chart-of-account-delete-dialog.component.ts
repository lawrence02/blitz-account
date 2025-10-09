import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IChartOfAccount } from '../chart-of-account.model';
import { ChartOfAccountService } from '../service/chart-of-account.service';

@Component({
  templateUrl: './chart-of-account-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ChartOfAccountDeleteDialogComponent {
  chartOfAccount?: IChartOfAccount;

  protected chartOfAccountService = inject(ChartOfAccountService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chartOfAccountService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
