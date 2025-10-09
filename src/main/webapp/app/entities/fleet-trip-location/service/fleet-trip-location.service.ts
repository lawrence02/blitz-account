import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFleetTripLocation, NewFleetTripLocation } from '../fleet-trip-location.model';

export type PartialUpdateFleetTripLocation = Partial<IFleetTripLocation> & Pick<IFleetTripLocation, 'id'>;

type RestOf<T extends IFleetTripLocation | NewFleetTripLocation> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestFleetTripLocation = RestOf<IFleetTripLocation>;

export type NewRestFleetTripLocation = RestOf<NewFleetTripLocation>;

export type PartialUpdateRestFleetTripLocation = RestOf<PartialUpdateFleetTripLocation>;

export type EntityResponseType = HttpResponse<IFleetTripLocation>;
export type EntityArrayResponseType = HttpResponse<IFleetTripLocation[]>;

@Injectable({ providedIn: 'root' })
export class FleetTripLocationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fleet-trip-locations');

  create(fleetTripLocation: NewFleetTripLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fleetTripLocation);
    return this.http
      .post<RestFleetTripLocation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fleetTripLocation: IFleetTripLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fleetTripLocation);
    return this.http
      .put<RestFleetTripLocation>(`${this.resourceUrl}/${this.getFleetTripLocationIdentifier(fleetTripLocation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fleetTripLocation: PartialUpdateFleetTripLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fleetTripLocation);
    return this.http
      .patch<RestFleetTripLocation>(`${this.resourceUrl}/${this.getFleetTripLocationIdentifier(fleetTripLocation)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFleetTripLocation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFleetTripLocation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFleetTripLocationIdentifier(fleetTripLocation: Pick<IFleetTripLocation, 'id'>): number {
    return fleetTripLocation.id;
  }

  compareFleetTripLocation(o1: Pick<IFleetTripLocation, 'id'> | null, o2: Pick<IFleetTripLocation, 'id'> | null): boolean {
    return o1 && o2 ? this.getFleetTripLocationIdentifier(o1) === this.getFleetTripLocationIdentifier(o2) : o1 === o2;
  }

  addFleetTripLocationToCollectionIfMissing<Type extends Pick<IFleetTripLocation, 'id'>>(
    fleetTripLocationCollection: Type[],
    ...fleetTripLocationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fleetTripLocations: Type[] = fleetTripLocationsToCheck.filter(isPresent);
    if (fleetTripLocations.length > 0) {
      const fleetTripLocationCollectionIdentifiers = fleetTripLocationCollection.map(fleetTripLocationItem =>
        this.getFleetTripLocationIdentifier(fleetTripLocationItem),
      );
      const fleetTripLocationsToAdd = fleetTripLocations.filter(fleetTripLocationItem => {
        const fleetTripLocationIdentifier = this.getFleetTripLocationIdentifier(fleetTripLocationItem);
        if (fleetTripLocationCollectionIdentifiers.includes(fleetTripLocationIdentifier)) {
          return false;
        }
        fleetTripLocationCollectionIdentifiers.push(fleetTripLocationIdentifier);
        return true;
      });
      return [...fleetTripLocationsToAdd, ...fleetTripLocationCollection];
    }
    return fleetTripLocationCollection;
  }

  protected convertDateFromClient<T extends IFleetTripLocation | NewFleetTripLocation | PartialUpdateFleetTripLocation>(
    fleetTripLocation: T,
  ): RestOf<T> {
    return {
      ...fleetTripLocation,
      timestamp: fleetTripLocation.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFleetTripLocation: RestFleetTripLocation): IFleetTripLocation {
    return {
      ...restFleetTripLocation,
      timestamp: restFleetTripLocation.timestamp ? dayjs(restFleetTripLocation.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFleetTripLocation>): HttpResponse<IFleetTripLocation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFleetTripLocation[]>): HttpResponse<IFleetTripLocation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
