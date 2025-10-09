import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFleetTripLocation } from '../fleet-trip-location.model';
import { FleetTripLocationService } from '../service/fleet-trip-location.service';

@Component({
  templateUrl: './fleet-trip-location-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FleetTripLocationDeleteDialogComponent {
  fleetTripLocation?: IFleetTripLocation;

  protected fleetTripLocationService = inject(FleetTripLocationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fleetTripLocationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
