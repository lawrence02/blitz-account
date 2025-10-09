import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IncidentType } from 'app/entities/enumerations/incident-type.model';
import { IIncidentLog } from '../incident-log.model';
import { IncidentLogService } from '../service/incident-log.service';
import { IncidentLogFormGroup, IncidentLogFormService } from './incident-log-form.service';

@Component({
  selector: 'jhi-incident-log-update',
  templateUrl: './incident-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class IncidentLogUpdateComponent implements OnInit {
  isSaving = false;
  incidentLog: IIncidentLog | null = null;
  incidentTypeValues = Object.keys(IncidentType);

  protected incidentLogService = inject(IncidentLogService);
  protected incidentLogFormService = inject(IncidentLogFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IncidentLogFormGroup = this.incidentLogFormService.createIncidentLogFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incidentLog }) => {
      this.incidentLog = incidentLog;
      if (incidentLog) {
        this.updateForm(incidentLog);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const incidentLog = this.incidentLogFormService.getIncidentLog(this.editForm);
    if (incidentLog.id !== null) {
      this.subscribeToSaveResponse(this.incidentLogService.update(incidentLog));
    } else {
      this.subscribeToSaveResponse(this.incidentLogService.create(incidentLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncidentLog>>): void {
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

  protected updateForm(incidentLog: IIncidentLog): void {
    this.incidentLog = incidentLog;
    this.incidentLogFormService.resetForm(this.editForm, incidentLog);
  }
}
