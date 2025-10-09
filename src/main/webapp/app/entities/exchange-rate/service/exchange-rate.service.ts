import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExchangeRate, NewExchangeRate } from '../exchange-rate.model';

export type PartialUpdateExchangeRate = Partial<IExchangeRate> & Pick<IExchangeRate, 'id'>;

type RestOf<T extends IExchangeRate | NewExchangeRate> = Omit<T, 'rateDate'> & {
  rateDate?: string | null;
};

export type RestExchangeRate = RestOf<IExchangeRate>;

export type NewRestExchangeRate = RestOf<NewExchangeRate>;

export type PartialUpdateRestExchangeRate = RestOf<PartialUpdateExchangeRate>;

export type EntityResponseType = HttpResponse<IExchangeRate>;
export type EntityArrayResponseType = HttpResponse<IExchangeRate[]>;

@Injectable({ providedIn: 'root' })
export class ExchangeRateService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/exchange-rates');

  create(exchangeRate: NewExchangeRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchangeRate);
    return this.http
      .post<RestExchangeRate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(exchangeRate: IExchangeRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchangeRate);
    return this.http
      .put<RestExchangeRate>(`${this.resourceUrl}/${this.getExchangeRateIdentifier(exchangeRate)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(exchangeRate: PartialUpdateExchangeRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchangeRate);
    return this.http
      .patch<RestExchangeRate>(`${this.resourceUrl}/${this.getExchangeRateIdentifier(exchangeRate)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExchangeRate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExchangeRate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExchangeRateIdentifier(exchangeRate: Pick<IExchangeRate, 'id'>): number {
    return exchangeRate.id;
  }

  compareExchangeRate(o1: Pick<IExchangeRate, 'id'> | null, o2: Pick<IExchangeRate, 'id'> | null): boolean {
    return o1 && o2 ? this.getExchangeRateIdentifier(o1) === this.getExchangeRateIdentifier(o2) : o1 === o2;
  }

  addExchangeRateToCollectionIfMissing<Type extends Pick<IExchangeRate, 'id'>>(
    exchangeRateCollection: Type[],
    ...exchangeRatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const exchangeRates: Type[] = exchangeRatesToCheck.filter(isPresent);
    if (exchangeRates.length > 0) {
      const exchangeRateCollectionIdentifiers = exchangeRateCollection.map(exchangeRateItem =>
        this.getExchangeRateIdentifier(exchangeRateItem),
      );
      const exchangeRatesToAdd = exchangeRates.filter(exchangeRateItem => {
        const exchangeRateIdentifier = this.getExchangeRateIdentifier(exchangeRateItem);
        if (exchangeRateCollectionIdentifiers.includes(exchangeRateIdentifier)) {
          return false;
        }
        exchangeRateCollectionIdentifiers.push(exchangeRateIdentifier);
        return true;
      });
      return [...exchangeRatesToAdd, ...exchangeRateCollection];
    }
    return exchangeRateCollection;
  }

  protected convertDateFromClient<T extends IExchangeRate | NewExchangeRate | PartialUpdateExchangeRate>(exchangeRate: T): RestOf<T> {
    return {
      ...exchangeRate,
      rateDate: exchangeRate.rateDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExchangeRate: RestExchangeRate): IExchangeRate {
    return {
      ...restExchangeRate,
      rateDate: restExchangeRate.rateDate ? dayjs(restExchangeRate.rateDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExchangeRate>): HttpResponse<IExchangeRate> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExchangeRate[]>): HttpResponse<IExchangeRate[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
