import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IServiceLog } from '../service-log.model';
import { ServiceLogService } from '../service/service-log.service';

@Component({
  templateUrl: './service-log-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceLogDeleteDialogComponent {
  serviceLog?: IServiceLog;

  protected serviceLogService = inject(ServiceLogService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceLogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
