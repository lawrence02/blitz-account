import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IServiceLog } from '../service-log.model';
import { ServiceLogService } from '../service/service-log.service';
import { ServiceLogFormGroup, ServiceLogFormService } from './service-log-form.service';

@Component({
  selector: 'jhi-service-log-update',
  templateUrl: './service-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ServiceLogUpdateComponent implements OnInit {
  isSaving = false;
  serviceLog: IServiceLog | null = null;

  protected serviceLogService = inject(ServiceLogService);
  protected serviceLogFormService = inject(ServiceLogFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServiceLogFormGroup = this.serviceLogFormService.createServiceLogFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceLog }) => {
      this.serviceLog = serviceLog;
      if (serviceLog) {
        this.updateForm(serviceLog);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceLog = this.serviceLogFormService.getServiceLog(this.editForm);
    if (serviceLog.id !== null) {
      this.subscribeToSaveResponse(this.serviceLogService.update(serviceLog));
    } else {
      this.subscribeToSaveResponse(this.serviceLogService.create(serviceLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceLog>>): void {
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

  protected updateForm(serviceLog: IServiceLog): void {
    this.serviceLog = serviceLog;
    this.serviceLogFormService.resetForm(this.editForm, serviceLog);
  }
}
