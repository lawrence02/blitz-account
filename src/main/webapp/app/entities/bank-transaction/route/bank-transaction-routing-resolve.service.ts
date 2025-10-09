import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBankTransaction } from '../bank-transaction.model';
import { BankTransactionService } from '../service/bank-transaction.service';

const bankTransactionResolve = (route: ActivatedRouteSnapshot): Observable<null | IBankTransaction> => {
  const id = route.params.id;
  if (id) {
    return inject(BankTransactionService)
      .find(id)
      .pipe(
        mergeMap((bankTransaction: HttpResponse<IBankTransaction>) => {
          if (bankTransaction.body) {
            return of(bankTransaction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bankTransactionResolve;
