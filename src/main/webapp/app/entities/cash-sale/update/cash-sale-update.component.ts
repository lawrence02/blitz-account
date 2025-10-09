import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICashSale } from '../cash-sale.model';
import { CashSaleService } from '../service/cash-sale.service';
import { CashSaleFormGroup, CashSaleFormService } from './cash-sale-form.service';

@Component({
  selector: 'jhi-cash-sale-update',
  templateUrl: './cash-sale-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CashSaleUpdateComponent implements OnInit {
  isSaving = false;
  cashSale: ICashSale | null = null;

  protected cashSaleService = inject(CashSaleService);
  protected cashSaleFormService = inject(CashSaleFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CashSaleFormGroup = this.cashSaleFormService.createCashSaleFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashSale }) => {
      this.cashSale = cashSale;
      if (cashSale) {
        this.updateForm(cashSale);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cashSale = this.cashSaleFormService.getCashSale(this.editForm);
    if (cashSale.id !== null) {
      this.subscribeToSaveResponse(this.cashSaleService.update(cashSale));
    } else {
      this.subscribeToSaveResponse(this.cashSaleService.create(cashSale));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashSale>>): void {
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

  protected updateForm(cashSale: ICashSale): void {
    this.cashSale = cashSale;
    this.cashSaleFormService.resetForm(this.editForm, cashSale);
  }
}
