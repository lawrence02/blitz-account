import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IInvoiceLine } from '../invoice-line.model';

@Component({
  selector: 'jhi-invoice-line-detail',
  templateUrl: './invoice-line-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class InvoiceLineDetailComponent {
  invoiceLine = input<IInvoiceLine | null>(null);

  previousState(): void {
    window.history.back();
  }
}
