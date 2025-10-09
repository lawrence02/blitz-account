import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFuelLog, NewFuelLog } from '../fuel-log.model';

export type PartialUpdateFuelLog = Partial<IFuelLog> & Pick<IFuelLog, 'id'>;

type RestOf<T extends IFuelLog | NewFuelLog> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestFuelLog = RestOf<IFuelLog>;

export type NewRestFuelLog = RestOf<NewFuelLog>;

export type PartialUpdateRestFuelLog = RestOf<PartialUpdateFuelLog>;

export type EntityResponseType = HttpResponse<IFuelLog>;
export type EntityArrayResponseType = HttpResponse<IFuelLog[]>;

@Injectable({ providedIn: 'root' })
export class FuelLogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fuel-logs');

  create(fuelLog: NewFuelLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fuelLog);
    return this.http
      .post<RestFuelLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fuelLog: IFuelLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fuelLog);
    return this.http
      .put<RestFuelLog>(`${this.resourceUrl}/${this.getFuelLogIdentifier(fuelLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fuelLog: PartialUpdateFuelLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fuelLog);
    return this.http
      .patch<RestFuelLog>(`${this.resourceUrl}/${this.getFuelLogIdentifier(fuelLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFuelLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFuelLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFuelLogIdentifier(fuelLog: Pick<IFuelLog, 'id'>): number {
    return fuelLog.id;
  }

  compareFuelLog(o1: Pick<IFuelLog, 'id'> | null, o2: Pick<IFuelLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getFuelLogIdentifier(o1) === this.getFuelLogIdentifier(o2) : o1 === o2;
  }

  addFuelLogToCollectionIfMissing<Type extends Pick<IFuelLog, 'id'>>(
    fuelLogCollection: Type[],
    ...fuelLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fuelLogs: Type[] = fuelLogsToCheck.filter(isPresent);
    if (fuelLogs.length > 0) {
      const fuelLogCollectionIdentifiers = fuelLogCollection.map(fuelLogItem => this.getFuelLogIdentifier(fuelLogItem));
      const fuelLogsToAdd = fuelLogs.filter(fuelLogItem => {
        const fuelLogIdentifier = this.getFuelLogIdentifier(fuelLogItem);
        if (fuelLogCollectionIdentifiers.includes(fuelLogIdentifier)) {
          return false;
        }
        fuelLogCollectionIdentifiers.push(fuelLogIdentifier);
        return true;
      });
      return [...fuelLogsToAdd, ...fuelLogCollection];
    }
    return fuelLogCollection;
  }

  protected convertDateFromClient<T extends IFuelLog | NewFuelLog | PartialUpdateFuelLog>(fuelLog: T): RestOf<T> {
    return {
      ...fuelLog,
      date: fuelLog.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFuelLog: RestFuelLog): IFuelLog {
    return {
      ...restFuelLog,
      date: restFuelLog.date ? dayjs(restFuelLog.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFuelLog>): HttpResponse<IFuelLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFuelLog[]>): HttpResponse<IFuelLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
