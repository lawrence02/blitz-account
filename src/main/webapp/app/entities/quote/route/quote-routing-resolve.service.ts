import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuote } from '../quote.model';
import { QuoteService } from '../service/quote.service';

const quoteResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuote> => {
  const id = route.params.id;
  if (id) {
    return inject(QuoteService)
      .find(id)
      .pipe(
        mergeMap((quote: HttpResponse<IQuote>) => {
          if (quote.body) {
            return of(quote.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default quoteResolve;
