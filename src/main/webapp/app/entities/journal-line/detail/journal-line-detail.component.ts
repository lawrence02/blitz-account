import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IJournalLine } from '../journal-line.model';

@Component({
  selector: 'jhi-journal-line-detail',
  templateUrl: './journal-line-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class JournalLineDetailComponent {
  journalLine = input<IJournalLine | null>(null);

  previousState(): void {
    window.history.back();
  }
}
