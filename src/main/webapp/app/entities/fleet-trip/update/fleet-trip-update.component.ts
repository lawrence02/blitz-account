import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { NgxSelectModule } from 'ngx-select-ex';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { FleetTripService } from '../service/fleet-trip.service';
import { IFleetTrip } from '../fleet-trip.model';
import { FleetTripFormGroup, FleetTripFormService } from './fleet-trip-form.service';

@Component({
  selector: 'jhi-fleet-trip-update',
  templateUrl: './fleet-trip-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgxSelectModule],
})
export class FleetTripUpdateComponent implements OnInit {
  isSaving = false;
  fleetTrip: IFleetTrip | null = null;

  vehicles: any[] = [
    { id: '1', name: 'Dump Truck' },
    { id: '2', name: 'Flatbed Truck' },
    { id: '3', name: 'Box Truck' },
  ];
  drivers: any[] = [
    { id: '1', name: 'John Doe' },
    { id: '2', name: 'Jane Smith' },
    { id: '3', name: 'Bob Johnson' },
  ];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected fleetTripService = inject(FleetTripService);
  protected fleetTripFormService = inject(FleetTripFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FleetTripFormGroup = this.fleetTripFormService.createFleetTripFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fleetTrip }) => {
      this.fleetTrip = fleetTrip;
      if (fleetTrip) {
        this.updateForm(fleetTrip);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('blitzAccountApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fleetTrip = this.fleetTripFormService.getFleetTrip(this.editForm);
    if (fleetTrip.id !== null) {
      this.subscribeToSaveResponse(this.fleetTripService.update(fleetTrip));
    } else {
      this.subscribeToSaveResponse(this.fleetTripService.create(fleetTrip));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFleetTrip>>): void {
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

  protected updateForm(fleetTrip: IFleetTrip): void {
    this.fleetTrip = fleetTrip;
    this.fleetTripFormService.resetForm(this.editForm, fleetTrip);
  }
}
