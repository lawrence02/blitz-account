import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFleetTrip, NewFleetTrip } from '../fleet-trip.model';

export type PartialUpdateFleetTrip = Partial<IFleetTrip> & Pick<IFleetTrip, 'id'>;

type RestOf<T extends IFleetTrip | NewFleetTrip> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestFleetTrip = RestOf<IFleetTrip>;

export type NewRestFleetTrip = RestOf<NewFleetTrip>;

export type PartialUpdateRestFleetTrip = RestOf<PartialUpdateFleetTrip>;

export type EntityResponseType = HttpResponse<IFleetTrip>;
export type EntityArrayResponseType = HttpResponse<IFleetTrip[]>;

@Injectable({ providedIn: 'root' })
export class FleetTripService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fleet-trips');

  create(fleetTrip: NewFleetTrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fleetTrip);
    return this.http
      .post<RestFleetTrip>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fleetTrip: IFleetTrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fleetTrip);
    return this.http
      .put<RestFleetTrip>(`${this.resourceUrl}/${this.getFleetTripIdentifier(fleetTrip)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fleetTrip: PartialUpdateFleetTrip): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fleetTrip);
    return this.http
      .patch<RestFleetTrip>(`${this.resourceUrl}/${this.getFleetTripIdentifier(fleetTrip)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFleetTrip>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFleetTrip[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFleetTripIdentifier(fleetTrip: Pick<IFleetTrip, 'id'>): number {
    return fleetTrip.id;
  }

  compareFleetTrip(o1: Pick<IFleetTrip, 'id'> | null, o2: Pick<IFleetTrip, 'id'> | null): boolean {
    return o1 && o2 ? this.getFleetTripIdentifier(o1) === this.getFleetTripIdentifier(o2) : o1 === o2;
  }

  addFleetTripToCollectionIfMissing<Type extends Pick<IFleetTrip, 'id'>>(
    fleetTripCollection: Type[],
    ...fleetTripsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fleetTrips: Type[] = fleetTripsToCheck.filter(isPresent);
    if (fleetTrips.length > 0) {
      const fleetTripCollectionIdentifiers = fleetTripCollection.map(fleetTripItem => this.getFleetTripIdentifier(fleetTripItem));
      const fleetTripsToAdd = fleetTrips.filter(fleetTripItem => {
        const fleetTripIdentifier = this.getFleetTripIdentifier(fleetTripItem);
        if (fleetTripCollectionIdentifiers.includes(fleetTripIdentifier)) {
          return false;
        }
        fleetTripCollectionIdentifiers.push(fleetTripIdentifier);
        return true;
      });
      return [...fleetTripsToAdd, ...fleetTripCollection];
    }
    return fleetTripCollection;
  }

  protected convertDateFromClient<T extends IFleetTrip | NewFleetTrip | PartialUpdateFleetTrip>(fleetTrip: T): RestOf<T> {
    return {
      ...fleetTrip,
      startDate: fleetTrip.startDate?.toJSON() ?? null,
      endDate: fleetTrip.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFleetTrip: RestFleetTrip): IFleetTrip {
    return {
      ...restFleetTrip,
      startDate: restFleetTrip.startDate ? dayjs(restFleetTrip.startDate) : undefined,
      endDate: restFleetTrip.endDate ? dayjs(restFleetTrip.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFleetTrip>): HttpResponse<IFleetTrip> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFleetTrip[]>): HttpResponse<IFleetTrip[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
