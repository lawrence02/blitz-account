import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInvoiceLine } from '../invoice-line.model';
import { InvoiceLineService } from '../service/invoice-line.service';
import { InvoiceLineFormGroup, InvoiceLineFormService } from './invoice-line-form.service';

@Component({
  selector: 'jhi-invoice-line-update',
  templateUrl: './invoice-line-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InvoiceLineUpdateComponent implements OnInit {
  isSaving = false;
  invoiceLine: IInvoiceLine | null = null;

  protected invoiceLineService = inject(InvoiceLineService);
  protected invoiceLineFormService = inject(InvoiceLineFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceLineFormGroup = this.invoiceLineFormService.createInvoiceLineFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceLine }) => {
      this.invoiceLine = invoiceLine;
      if (invoiceLine) {
        this.updateForm(invoiceLine);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceLine = this.invoiceLineFormService.getInvoiceLine(this.editForm);
    if (invoiceLine.id !== null) {
      this.subscribeToSaveResponse(this.invoiceLineService.update(invoiceLine));
    } else {
      this.subscribeToSaveResponse(this.invoiceLineService.create(invoiceLine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceLine>>): void {
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

  protected updateForm(invoiceLine: IInvoiceLine): void {
    this.invoiceLine = invoiceLine;
    this.invoiceLineFormService.resetForm(this.editForm, invoiceLine);
  }
}
