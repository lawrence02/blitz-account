import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IQuoteLine } from '../quote-line.model';
import { QuoteLineService } from '../service/quote-line.service';
import { QuoteLineFormGroup, QuoteLineFormService } from './quote-line-form.service';

@Component({
  selector: 'jhi-quote-line-update',
  templateUrl: './quote-line-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuoteLineUpdateComponent implements OnInit {
  isSaving = false;
  quoteLine: IQuoteLine | null = null;

  protected quoteLineService = inject(QuoteLineService);
  protected quoteLineFormService = inject(QuoteLineFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuoteLineFormGroup = this.quoteLineFormService.createQuoteLineFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quoteLine }) => {
      this.quoteLine = quoteLine;
      if (quoteLine) {
        this.updateForm(quoteLine);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quoteLine = this.quoteLineFormService.getQuoteLine(this.editForm);
    if (quoteLine.id !== null) {
      this.subscribeToSaveResponse(this.quoteLineService.update(quoteLine));
    } else {
      this.subscribeToSaveResponse(this.quoteLineService.create(quoteLine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuoteLine>>): void {
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

  protected updateForm(quoteLine: IQuoteLine): void {
    this.quoteLine = quoteLine;
    this.quoteLineFormService.resetForm(this.editForm, quoteLine);
  }
}
