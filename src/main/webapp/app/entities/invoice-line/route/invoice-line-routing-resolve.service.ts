import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvoiceLine } from '../invoice-line.model';
import { InvoiceLineService } from '../service/invoice-line.service';

const invoiceLineResolve = (route: ActivatedRouteSnapshot): Observable<null | IInvoiceLine> => {
  const id = route.params.id;
  if (id) {
    return inject(InvoiceLineService)
      .find(id)
      .pipe(
        mergeMap((invoiceLine: HttpResponse<IInvoiceLine>) => {
          if (invoiceLine.body) {
            return of(invoiceLine.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default invoiceLineResolve;
