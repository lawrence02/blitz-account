import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFleetTripLocation } from '../fleet-trip-location.model';
import { FleetTripLocationService } from '../service/fleet-trip-location.service';

const fleetTripLocationResolve = (route: ActivatedRouteSnapshot): Observable<null | IFleetTripLocation> => {
  const id = route.params.id;
  if (id) {
    return inject(FleetTripLocationService)
      .find(id)
      .pipe(
        mergeMap((fleetTripLocation: HttpResponse<IFleetTripLocation>) => {
          if (fleetTripLocation.body) {
            return of(fleetTripLocation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default fleetTripLocationResolve;
