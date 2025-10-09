import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQuote } from '../quote.model';
import { QuoteService } from '../service/quote.service';

@Component({
  templateUrl: './quote-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuoteDeleteDialogComponent {
  quote?: IQuote;

  protected quoteService = inject(QuoteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quoteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
