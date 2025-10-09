import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecurringTransaction } from '../recurring-transaction.model';
import { RecurringTransactionService } from '../service/recurring-transaction.service';

const recurringTransactionResolve = (route: ActivatedRouteSnapshot): Observable<null | IRecurringTransaction> => {
  const id = route.params.id;
  if (id) {
    return inject(RecurringTransactionService)
      .find(id)
      .pipe(
        mergeMap((recurringTransaction: HttpResponse<IRecurringTransaction>) => {
          if (recurringTransaction.body) {
            return of(recurringTransaction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default recurringTransactionResolve;
