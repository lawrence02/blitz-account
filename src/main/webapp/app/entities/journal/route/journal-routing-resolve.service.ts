import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJournal } from '../journal.model';
import { JournalService } from '../service/journal.service';

const journalResolve = (route: ActivatedRouteSnapshot): Observable<null | IJournal> => {
  const id = route.params.id;
  if (id) {
    return inject(JournalService)
      .find(id)
      .pipe(
        mergeMap((journal: HttpResponse<IJournal>) => {
          if (journal.body) {
            return of(journal.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default journalResolve;
