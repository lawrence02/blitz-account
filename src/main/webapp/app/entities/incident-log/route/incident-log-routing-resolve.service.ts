import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIncidentLog } from '../incident-log.model';
import { IncidentLogService } from '../service/incident-log.service';

const incidentLogResolve = (route: ActivatedRouteSnapshot): Observable<null | IIncidentLog> => {
  const id = route.params.id;
  if (id) {
    return inject(IncidentLogService)
      .find(id)
      .pipe(
        mergeMap((incidentLog: HttpResponse<IIncidentLog>) => {
          if (incidentLog.body) {
            return of(incidentLog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default incidentLogResolve;
