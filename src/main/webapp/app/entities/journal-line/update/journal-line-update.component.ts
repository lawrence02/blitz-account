import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IJournalLine } from '../journal-line.model';
import { JournalLineService } from '../service/journal-line.service';
import { JournalLineFormGroup, JournalLineFormService } from './journal-line-form.service';

@Component({
  selector: 'jhi-journal-line-update',
  templateUrl: './journal-line-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class JournalLineUpdateComponent implements OnInit {
  isSaving = false;
  journalLine: IJournalLine | null = null;

  protected journalLineService = inject(JournalLineService);
  protected journalLineFormService = inject(JournalLineFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: JournalLineFormGroup = this.journalLineFormService.createJournalLineFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ journalLine }) => {
      this.journalLine = journalLine;
      if (journalLine) {
        this.updateForm(journalLine);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const journalLine = this.journalLineFormService.getJournalLine(this.editForm);
    if (journalLine.id !== null) {
      this.subscribeToSaveResponse(this.journalLineService.update(journalLine));
    } else {
      this.subscribeToSaveResponse(this.journalLineService.create(journalLine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJournalLine>>): void {
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

  protected updateForm(journalLine: IJournalLine): void {
    this.journalLine = journalLine;
    this.journalLineFormService.resetForm(this.editForm, journalLine);
  }
}
