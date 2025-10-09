import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AccountType } from 'app/entities/enumerations/account-type.model';
import { IChartOfAccount } from '../chart-of-account.model';
import { ChartOfAccountService } from '../service/chart-of-account.service';
import { ChartOfAccountFormGroup, ChartOfAccountFormService } from './chart-of-account-form.service';

@Component({
  selector: 'jhi-chart-of-account-update',
  templateUrl: './chart-of-account-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ChartOfAccountUpdateComponent implements OnInit {
  isSaving = false;
  chartOfAccount: IChartOfAccount | null = null;
  accountTypeValues = Object.keys(AccountType);

  protected chartOfAccountService = inject(ChartOfAccountService);
  protected chartOfAccountFormService = inject(ChartOfAccountFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ChartOfAccountFormGroup = this.chartOfAccountFormService.createChartOfAccountFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chartOfAccount }) => {
      this.chartOfAccount = chartOfAccount;
      if (chartOfAccount) {
        this.updateForm(chartOfAccount);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chartOfAccount = this.chartOfAccountFormService.getChartOfAccount(this.editForm);
    if (chartOfAccount.id !== null) {
      this.subscribeToSaveResponse(this.chartOfAccountService.update(chartOfAccount));
    } else {
      this.subscribeToSaveResponse(this.chartOfAccountService.create(chartOfAccount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChartOfAccount>>): void {
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

  protected updateForm(chartOfAccount: IChartOfAccount): void {
    this.chartOfAccount = chartOfAccount;
    this.chartOfAccountFormService.resetForm(this.editForm, chartOfAccount);
  }
}
