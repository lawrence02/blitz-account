import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFuelLog } from '../fuel-log.model';
import { FuelLogService } from '../service/fuel-log.service';
import { FuelLogFormGroup, FuelLogFormService } from './fuel-log-form.service';

@Component({
  selector: 'jhi-fuel-log-update',
  templateUrl: './fuel-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FuelLogUpdateComponent implements OnInit {
  isSaving = false;
  fuelLog: IFuelLog | null = null;

  protected fuelLogService = inject(FuelLogService);
  protected fuelLogFormService = inject(FuelLogFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FuelLogFormGroup = this.fuelLogFormService.createFuelLogFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fuelLog }) => {
      this.fuelLog = fuelLog;
      if (fuelLog) {
        this.updateForm(fuelLog);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fuelLog = this.fuelLogFormService.getFuelLog(this.editForm);
    if (fuelLog.id !== null) {
      this.subscribeToSaveResponse(this.fuelLogService.update(fuelLog));
    } else {
      this.subscribeToSaveResponse(this.fuelLogService.create(fuelLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFuelLog>>): void {
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

  protected updateForm(fuelLog: IFuelLog): void {
    this.fuelLog = fuelLog;
    this.fuelLogFormService.resetForm(this.editForm, fuelLog);
  }
}
