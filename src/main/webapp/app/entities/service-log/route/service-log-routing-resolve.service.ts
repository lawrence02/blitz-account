import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceLog } from '../service-log.model';
import { ServiceLogService } from '../service/service-log.service';

const serviceLogResolve = (route: ActivatedRouteSnapshot): Observable<null | IServiceLog> => {
  const id = route.params.id;
  if (id) {
    return inject(ServiceLogService)
      .find(id)
      .pipe(
        mergeMap((serviceLog: HttpResponse<IServiceLog>) => {
          if (serviceLog.body) {
            return of(serviceLog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default serviceLogResolve;
