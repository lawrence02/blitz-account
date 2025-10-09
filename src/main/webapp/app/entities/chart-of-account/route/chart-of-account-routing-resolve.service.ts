import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChartOfAccount } from '../chart-of-account.model';
import { ChartOfAccountService } from '../service/chart-of-account.service';

const chartOfAccountResolve = (route: ActivatedRouteSnapshot): Observable<null | IChartOfAccount> => {
  const id = route.params.id;
  if (id) {
    return inject(ChartOfAccountService)
      .find(id)
      .pipe(
        mergeMap((chartOfAccount: HttpResponse<IChartOfAccount>) => {
          if (chartOfAccount.body) {
            return of(chartOfAccount.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default chartOfAccountResolve;
