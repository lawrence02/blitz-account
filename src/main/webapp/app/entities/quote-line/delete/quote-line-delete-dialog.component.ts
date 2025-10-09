import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQuoteLine } from '../quote-line.model';
import { QuoteLineService } from '../service/quote-line.service';

@Component({
  templateUrl: './quote-line-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuoteLineDeleteDialogComponent {
  quoteLine?: IQuoteLine;

  protected quoteLineService = inject(QuoteLineService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quoteLineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
