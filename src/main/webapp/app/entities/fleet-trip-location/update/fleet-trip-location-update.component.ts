import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFleetTripLocation } from '../fleet-trip-location.model';
import { FleetTripLocationService } from '../service/fleet-trip-location.service';
import { FleetTripLocationFormGroup, FleetTripLocationFormService } from './fleet-trip-location-form.service';

@Component({
  selector: 'jhi-fleet-trip-location-update',
  templateUrl: './fleet-trip-location-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FleetTripLocationUpdateComponent implements OnInit {
  isSaving = false;
  fleetTripLocation: IFleetTripLocation | null = null;

  protected fleetTripLocationService = inject(FleetTripLocationService);
  protected fleetTripLocationFormService = inject(FleetTripLocationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FleetTripLocationFormGroup = this.fleetTripLocationFormService.createFleetTripLocationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fleetTripLocation }) => {
      this.fleetTripLocation = fleetTripLocation;
      if (fleetTripLocation) {
        this.updateForm(fleetTripLocation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fleetTripLocation = this.fleetTripLocationFormService.getFleetTripLocation(this.editForm);
    if (fleetTripLocation.id !== null) {
      this.subscribeToSaveResponse(this.fleetTripLocationService.update(fleetTripLocation));
    } else {
      this.subscribeToSaveResponse(this.fleetTripLocationService.create(fleetTripLocation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFleetTripLocation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fleetTripLocation: IFleetTripLocation): void {
    this.fleetTripLocation = fleetTripLocation;
    this.fleetTripLocationFormService.resetForm(this.editForm, fleetTripLocation);
  }
}
