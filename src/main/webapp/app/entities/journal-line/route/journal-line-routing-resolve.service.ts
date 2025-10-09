import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJournalLine } from '../journal-line.model';
import { JournalLineService } from '../service/journal-line.service';

const journalLineResolve = (route: ActivatedRouteSnapshot): Observable<null | IJournalLine> => {
  const id = route.params.id;
  if (id) {
    return inject(JournalLineService)
      .find(id)
      .pipe(
        mergeMap((journalLine: HttpResponse<IJournalLine>) => {
          if (journalLine.body) {
            return of(journalLine.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default journalLineResolve;
