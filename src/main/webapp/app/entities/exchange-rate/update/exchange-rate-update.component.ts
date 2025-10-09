import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IExchangeRate } from '../exchange-rate.model';
import { ExchangeRateService } from '../service/exchange-rate.service';
import { ExchangeRateFormGroup, ExchangeRateFormService } from './exchange-rate-form.service';

@Component({
  selector: 'jhi-exchange-rate-update',
  templateUrl: './exchange-rate-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExchangeRateUpdateComponent implements OnInit {
  isSaving = false;
  exchangeRate: IExchangeRate | null = null;

  protected exchangeRateService = inject(ExchangeRateService);
  protected exchangeRateFormService = inject(ExchangeRateFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExchangeRateFormGroup = this.exchangeRateFormService.createExchangeRateFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exchangeRate }) => {
      this.exchangeRate = exchangeRate;
      if (exchangeRate) {
        this.updateForm(exchangeRate);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exchangeRate = this.exchangeRateFormService.getExchangeRate(this.editForm);
    if (exchangeRate.id !== null) {
      this.subscribeToSaveResponse(this.exchangeRateService.update(exchangeRate));
    } else {
      this.subscribeToSaveResponse(this.exchangeRateService.create(exchangeRate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExchangeRate>>): void {
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

  protected updateForm(exchangeRate: IExchangeRate): void {
    this.exchangeRate = exchangeRate;
    this.exchangeRateFormService.resetForm(this.editForm, exchangeRate);
  }
}
