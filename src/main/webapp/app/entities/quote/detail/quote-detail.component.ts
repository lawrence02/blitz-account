import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IQuote } from '../quote.model';

@Component({
  selector: 'jhi-quote-detail',
  templateUrl: './quote-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class QuoteDetailComponent {
  quote = input<IQuote | null>(null);

  previousState(): void {
    window.history.back();
  }
}
