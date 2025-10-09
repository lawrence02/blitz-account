import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICashSale } from '../cash-sale.model';
import { CashSaleService } from '../service/cash-sale.service';

@Component({
  templateUrl: './cash-sale-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CashSaleDeleteDialogComponent {
  cashSale?: ICashSale;

  protected cashSaleService = inject(CashSaleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cashSaleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
