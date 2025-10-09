import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceLog, NewServiceLog } from '../service-log.model';

export type PartialUpdateServiceLog = Partial<IServiceLog> & Pick<IServiceLog, 'id'>;

type RestOf<T extends IServiceLog | NewServiceLog> = Omit<T, 'serviceDate'> & {
  serviceDate?: string | null;
};

export type RestServiceLog = RestOf<IServiceLog>;

export type NewRestServiceLog = RestOf<NewServiceLog>;

export type PartialUpdateRestServiceLog = RestOf<PartialUpdateServiceLog>;

export type EntityResponseType = HttpResponse<IServiceLog>;
export type EntityArrayResponseType = HttpResponse<IServiceLog[]>;

@Injectable({ providedIn: 'root' })
export class ServiceLogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-logs');

  create(serviceLog: NewServiceLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceLog);
    return this.http
      .post<RestServiceLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(serviceLog: IServiceLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceLog);
    return this.http
      .put<RestServiceLog>(`${this.resourceUrl}/${this.getServiceLogIdentifier(serviceLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(serviceLog: PartialUpdateServiceLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceLog);
    return this.http
      .patch<RestServiceLog>(`${this.resourceUrl}/${this.getServiceLogIdentifier(serviceLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestServiceLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServiceLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getServiceLogIdentifier(serviceLog: Pick<IServiceLog, 'id'>): number {
    return serviceLog.id;
  }

  compareServiceLog(o1: Pick<IServiceLog, 'id'> | null, o2: Pick<IServiceLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceLogIdentifier(o1) === this.getServiceLogIdentifier(o2) : o1 === o2;
  }

  addServiceLogToCollectionIfMissing<Type extends Pick<IServiceLog, 'id'>>(
    serviceLogCollection: Type[],
    ...serviceLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceLogs: Type[] = serviceLogsToCheck.filter(isPresent);
    if (serviceLogs.length > 0) {
      const serviceLogCollectionIdentifiers = serviceLogCollection.map(serviceLogItem => this.getServiceLogIdentifier(serviceLogItem));
      const serviceLogsToAdd = serviceLogs.filter(serviceLogItem => {
        const serviceLogIdentifier = this.getServiceLogIdentifier(serviceLogItem);
        if (serviceLogCollectionIdentifiers.includes(serviceLogIdentifier)) {
          return false;
        }
        serviceLogCollectionIdentifiers.push(serviceLogIdentifier);
        return true;
      });
      return [...serviceLogsToAdd, ...serviceLogCollection];
    }
    return serviceLogCollection;
  }

  protected convertDateFromClient<T extends IServiceLog | NewServiceLog | PartialUpdateServiceLog>(serviceLog: T): RestOf<T> {
    return {
      ...serviceLog,
      serviceDate: serviceLog.serviceDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restServiceLog: RestServiceLog): IServiceLog {
    return {
      ...restServiceLog,
      serviceDate: restServiceLog.serviceDate ? dayjs(restServiceLog.serviceDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestServiceLog>): HttpResponse<IServiceLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestServiceLog[]>): HttpResponse<IServiceLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
