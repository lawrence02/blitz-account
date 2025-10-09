import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuoteLine } from '../quote-line.model';
import { QuoteLineService } from '../service/quote-line.service';

const quoteLineResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuoteLine> => {
  const id = route.params.id;
  if (id) {
    return inject(QuoteLineService)
      .find(id)
      .pipe(
        mergeMap((quoteLine: HttpResponse<IQuoteLine>) => {
          if (quoteLine.body) {
            return of(quoteLine.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default quoteLineResolve;
