import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVATRate } from '../vat-rate.model';
import { VATRateService } from '../service/vat-rate.service';

const vATRateResolve = (route: ActivatedRouteSnapshot): Observable<null | IVATRate> => {
  const id = route.params.id;
  if (id) {
    return inject(VATRateService)
      .find(id)
      .pipe(
        mergeMap((vATRate: HttpResponse<IVATRate>) => {
          if (vATRate.body) {
            return of(vATRate.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vATRateResolve;
