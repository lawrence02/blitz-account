import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFleetTrip } from '../fleet-trip.model';
import { FleetTripService } from '../service/fleet-trip.service';

const fleetTripResolve = (route: ActivatedRouteSnapshot): Observable<null | IFleetTrip> => {
  const id = route.params.id;
  if (id) {
    return inject(FleetTripService)
      .find(id)
      .pipe(
        mergeMap((fleetTrip: HttpResponse<IFleetTrip>) => {
          if (fleetTrip.body) {
            return of(fleetTrip.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default fleetTripResolve;
