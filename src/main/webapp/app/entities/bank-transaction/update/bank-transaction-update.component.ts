import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TransactionDirection } from 'app/entities/enumerations/transaction-direction.model';
import { IBankTransaction } from '../bank-transaction.model';
import { BankTransactionService } from '../service/bank-transaction.service';
import { BankTransactionFormGroup, BankTransactionFormService } from './bank-transaction-form.service';

@Component({
  selector: 'jhi-bank-transaction-update',
  templateUrl: './bank-transaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BankTransactionUpdateComponent implements OnInit {
  isSaving = false;
  bankTransaction: IBankTransaction | null = null;
  transactionDirectionValues = Object.keys(TransactionDirection);

  protected bankTransactionService = inject(BankTransactionService);
  protected bankTransactionFormService = inject(BankTransactionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BankTransactionFormGroup = this.bankTransactionFormService.createBankTransactionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankTransaction }) => {
      this.bankTransaction = bankTransaction;
      if (bankTransaction) {
        this.updateForm(bankTransaction);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankTransaction = this.bankTransactionFormService.getBankTransaction(this.editForm);
    if (bankTransaction.id !== null) {
      this.subscribeToSaveResponse(this.bankTransactionService.update(bankTransaction));
    } else {
      this.subscribeToSaveResponse(this.bankTransactionService.create(bankTransaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankTransaction>>): void {
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

  protected updateForm(bankTransaction: IBankTransaction): void {
    this.bankTransaction = bankTransaction;
    this.bankTransactionFormService.resetForm(this.editForm, bankTransaction);
  }
}
