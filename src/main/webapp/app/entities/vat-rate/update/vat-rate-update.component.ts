import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVATRate } from '../vat-rate.model';
import { VATRateService } from '../service/vat-rate.service';
import { VATRateFormGroup, VATRateFormService } from './vat-rate-form.service';

@Component({
  selector: 'jhi-vat-rate-update',
  templateUrl: './vat-rate-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VATRateUpdateComponent implements OnInit {
  isSaving = false;
  vATRate: IVATRate | null = null;

  protected vATRateService = inject(VATRateService);
  protected vATRateFormService = inject(VATRateFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VATRateFormGroup = this.vATRateFormService.createVATRateFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vATRate }) => {
      this.vATRate = vATRate;
      if (vATRate) {
        this.updateForm(vATRate);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vATRate = this.vATRateFormService.getVATRate(this.editForm);
    if (vATRate.id !== null) {
      this.subscribeToSaveResponse(this.vATRateService.update(vATRate));
    } else {
      this.subscribeToSaveResponse(this.vATRateService.create(vATRate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVATRate>>): void {
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

  protected updateForm(vATRate: IVATRate): void {
    this.vATRate = vATRate;
    this.vATRateFormService.resetForm(this.editForm, vATRate);
  }
}
