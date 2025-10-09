import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IJournalLine } from '../journal-line.model';
import { JournalLineService } from '../service/journal-line.service';

@Component({
  templateUrl: './journal-line-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class JournalLineDeleteDialogComponent {
  journalLine?: IJournalLine;

  protected journalLineService = inject(JournalLineService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.journalLineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
