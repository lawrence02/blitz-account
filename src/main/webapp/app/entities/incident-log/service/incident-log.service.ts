import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIncidentLog, IIncidentStats, NewIncidentLog } from '../incident-log.model';

export type PartialUpdateIncidentLog = Partial<IIncidentLog> & Pick<IIncidentLog, 'id'>;

type RestOf<T extends IIncidentLog | NewIncidentLog> = Omit<T, 'incidentDate'> & {
  incidentDate?: string | null;
};

export type RestIncidentLog = RestOf<IIncidentLog>;

export type NewRestIncidentLog = RestOf<NewIncidentLog>;

export type PartialUpdateRestIncidentLog = RestOf<PartialUpdateIncidentLog>;

export type EntityResponseType = HttpResponse<IIncidentLog>;
export type EntityArrayResponseType = HttpResponse<IIncidentLog[]>;

@Injectable({ providedIn: 'root' })
export class IncidentLogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/incident-logs');

  create(incidentLog: NewIncidentLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incidentLog);
    return this.http
      .post<RestIncidentLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(incidentLog: IIncidentLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incidentLog);
    return this.http
      .put<RestIncidentLog>(`${this.resourceUrl}/${this.getIncidentLogIdentifier(incidentLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(incidentLog: PartialUpdateIncidentLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incidentLog);
    return this.http
      .patch<RestIncidentLog>(`${this.resourceUrl}/${this.getIncidentLogIdentifier(incidentLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIncidentLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  getIncidentStats(): Observable<HttpResponse<IIncidentStats>> {
    return this.http.get<IIncidentStats>(this.resourceUrl + '/incident-stats', { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIncidentLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIncidentLogIdentifier(incidentLog: Pick<IIncidentLog, 'id'>): number {
    return incidentLog.id;
  }

  compareIncidentLog(o1: Pick<IIncidentLog, 'id'> | null, o2: Pick<IIncidentLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getIncidentLogIdentifier(o1) === this.getIncidentLogIdentifier(o2) : o1 === o2;
  }

  addIncidentLogToCollectionIfMissing<Type extends Pick<IIncidentLog, 'id'>>(
    incidentLogCollection: Type[],
    ...incidentLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const incidentLogs: Type[] = incidentLogsToCheck.filter(isPresent);
    if (incidentLogs.length > 0) {
      const incidentLogCollectionIdentifiers = incidentLogCollection.map(incidentLogItem => this.getIncidentLogIdentifier(incidentLogItem));
      const incidentLogsToAdd = incidentLogs.filter(incidentLogItem => {
        const incidentLogIdentifier = this.getIncidentLogIdentifier(incidentLogItem);
        if (incidentLogCollectionIdentifiers.includes(incidentLogIdentifier)) {
          return false;
        }
        incidentLogCollectionIdentifiers.push(incidentLogIdentifier);
        return true;
      });
      return [...incidentLogsToAdd, ...incidentLogCollection];
    }
    return incidentLogCollection;
  }

  protected convertDateFromClient<T extends IIncidentLog | NewIncidentLog | PartialUpdateIncidentLog>(incidentLog: T): RestOf<T> {
    return {
      ...incidentLog,
      incidentDate: incidentLog.incidentDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restIncidentLog: RestIncidentLog): IIncidentLog {
    return {
      ...restIncidentLog,
      incidentDate: restIncidentLog.incidentDate ? dayjs(restIncidentLog.incidentDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIncidentLog>): HttpResponse<IIncidentLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIncidentLog[]>): HttpResponse<IIncidentLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
