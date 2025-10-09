import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRecurringTransaction } from '../recurring-transaction.model';
import { RecurringTransactionService } from '../service/recurring-transaction.service';
import { RecurringTransactionFormGroup, RecurringTransactionFormService } from './recurring-transaction-form.service';

@Component({
  selector: 'jhi-recurring-transaction-update',
  templateUrl: './recurring-transaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RecurringTransactionUpdateComponent implements OnInit {
  isSaving = false;
  recurringTransaction: IRecurringTransaction | null = null;

  protected recurringTransactionService = inject(RecurringTransactionService);
  protected recurringTransactionFormService = inject(RecurringTransactionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RecurringTransactionFormGroup = this.recurringTransactionFormService.createRecurringTransactionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recurringTransaction }) => {
      this.recurringTransaction = recurringTransaction;
      if (recurringTransaction) {
        this.updateForm(recurringTransaction);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recurringTransaction = this.recurringTransactionFormService.getRecurringTransaction(this.editForm);
    if (recurringTransaction.id !== null) {
      this.subscribeToSaveResponse(this.recurringTransactionService.update(recurringTransaction));
    } else {
      this.subscribeToSaveResponse(this.recurringTransactionService.create(recurringTransaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecurringTransaction>>): void {
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

  protected updateForm(recurringTransaction: IRecurringTransaction): void {
    this.recurringTransaction = recurringTransaction;
    this.recurringTransactionFormService.resetForm(this.editForm, recurringTransaction);
  }
}
