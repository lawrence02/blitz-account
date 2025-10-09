import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IJournal } from '../journal.model';
import { JournalService } from '../service/journal.service';

@Component({
  templateUrl: './journal-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class JournalDeleteDialogComponent {
  journal?: IJournal;

  protected journalService = inject(JournalService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.journalService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
