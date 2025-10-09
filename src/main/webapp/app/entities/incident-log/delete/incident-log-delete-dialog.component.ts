import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIncidentLog } from '../incident-log.model';
import { IncidentLogService } from '../service/incident-log.service';

@Component({
  templateUrl: './incident-log-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class IncidentLogDeleteDialogComponent {
  incidentLog?: IIncidentLog;

  protected incidentLogService = inject(IncidentLogService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incidentLogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
