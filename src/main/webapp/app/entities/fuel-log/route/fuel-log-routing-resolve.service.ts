import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFuelLog } from '../fuel-log.model';
import { FuelLogService } from '../service/fuel-log.service';

const fuelLogResolve = (route: ActivatedRouteSnapshot): Observable<null | IFuelLog> => {
  const id = route.params.id;
  if (id) {
    return inject(FuelLogService)
      .find(id)
      .pipe(
        mergeMap((fuelLog: HttpResponse<IFuelLog>) => {
          if (fuelLog.body) {
            return of(fuelLog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default fuelLogResolve;
