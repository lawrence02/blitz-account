import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { DocumentStatus } from 'app/entities/enumerations/document-status.model';
import { IQuote } from '../quote.model';
import { QuoteService } from '../service/quote.service';
import { QuoteFormGroup, QuoteFormService } from './quote-form.service';

@Component({
  selector: 'jhi-quote-update',
  templateUrl: './quote-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuoteUpdateComponent implements OnInit {
  isSaving = false;
  quote: IQuote | null = null;
  documentStatusValues = Object.keys(DocumentStatus);

  protected quoteService = inject(QuoteService);
  protected quoteFormService = inject(QuoteFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuoteFormGroup = this.quoteFormService.createQuoteFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quote }) => {
      this.quote = quote;
      if (quote) {
        this.updateForm(quote);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quote = this.quoteFormService.getQuote(this.editForm);
    if (quote.id !== null) {
      this.subscribeToSaveResponse(this.quoteService.update(quote));
    } else {
      this.subscribeToSaveResponse(this.quoteService.create(quote));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuote>>): void {
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

  protected updateForm(quote: IQuote): void {
    this.quote = quote;
    this.quoteFormService.resetForm(this.editForm, quote);
  }
}
