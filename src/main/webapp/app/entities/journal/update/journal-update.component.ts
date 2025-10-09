import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IJournal } from '../journal.model';
import { JournalService } from '../service/journal.service';
import { JournalFormGroup, JournalFormService } from './journal-form.service';

@Component({
  selector: 'jhi-journal-update',
  templateUrl: './journal-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class JournalUpdateComponent implements OnInit {
  isSaving = false;
  journal: IJournal | null = null;

  protected journalService = inject(JournalService);
  protected journalFormService = inject(JournalFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: JournalFormGroup = this.journalFormService.createJournalFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ journal }) => {
      this.journal = journal;
      if (journal) {
        this.updateForm(journal);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const journal = this.journalFormService.getJournal(this.editForm);
    if (journal.id !== null) {
      this.subscribeToSaveResponse(this.journalService.update(journal));
    } else {
      this.subscribeToSaveResponse(this.journalService.create(journal));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJournal>>): void {
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

  protected updateForm(journal: IJournal): void {
    this.journal = journal;
    this.journalFormService.resetForm(this.editForm, journal);
  }
}
