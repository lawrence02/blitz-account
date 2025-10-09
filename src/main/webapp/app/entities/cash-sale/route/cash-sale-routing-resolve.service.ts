import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICashSale } from '../cash-sale.model';
import { CashSaleService } from '../service/cash-sale.service';

const cashSaleResolve = (route: ActivatedRouteSnapshot): Observable<null | ICashSale> => {
  const id = route.params.id;
  if (id) {
    return inject(CashSaleService)
      .find(id)
      .pipe(
        mergeMap((cashSale: HttpResponse<ICashSale>) => {
          if (cashSale.body) {
            return of(cashSale.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default cashSaleResolve;
