import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IQuoteLine } from '../quote-line.model';

@Component({
  selector: 'jhi-quote-line-detail',
  templateUrl: './quote-line-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class QuoteLineDetailComponent {
  quoteLine = input<IQuoteLine | null>(null);

  previousState(): void {
    window.history.back();
  }
}
