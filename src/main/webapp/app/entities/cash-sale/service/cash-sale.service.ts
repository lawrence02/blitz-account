import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICashSale, NewCashSale } from '../cash-sale.model';

export type PartialUpdateCashSale = Partial<ICashSale> & Pick<ICashSale, 'id'>;

type RestOf<T extends ICashSale | NewCashSale> = Omit<T, 'saleDate'> & {
  saleDate?: string | null;
};

export type RestCashSale = RestOf<ICashSale>;

export type NewRestCashSale = RestOf<NewCashSale>;

export type PartialUpdateRestCashSale = RestOf<PartialUpdateCashSale>;

export type EntityResponseType = HttpResponse<ICashSale>;
export type EntityArrayResponseType = HttpResponse<ICashSale[]>;

@Injectable({ providedIn: 'root' })
export class CashSaleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cash-sales');

  create(cashSale: NewCashSale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashSale);
    return this.http
      .post<RestCashSale>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cashSale: ICashSale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashSale);
    return this.http
      .put<RestCashSale>(`${this.resourceUrl}/${this.getCashSaleIdentifier(cashSale)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cashSale: PartialUpdateCashSale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashSale);
    return this.http
      .patch<RestCashSale>(`${this.resourceUrl}/${this.getCashSaleIdentifier(cashSale)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCashSale>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCashSale[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCashSaleIdentifier(cashSale: Pick<ICashSale, 'id'>): number {
    return cashSale.id;
  }

  compareCashSale(o1: Pick<ICashSale, 'id'> | null, o2: Pick<ICashSale, 'id'> | null): boolean {
    return o1 && o2 ? this.getCashSaleIdentifier(o1) === this.getCashSaleIdentifier(o2) : o1 === o2;
  }

  addCashSaleToCollectionIfMissing<Type extends Pick<ICashSale, 'id'>>(
    cashSaleCollection: Type[],
    ...cashSalesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cashSales: Type[] = cashSalesToCheck.filter(isPresent);
    if (cashSales.length > 0) {
      const cashSaleCollectionIdentifiers = cashSaleCollection.map(cashSaleItem => this.getCashSaleIdentifier(cashSaleItem));
      const cashSalesToAdd = cashSales.filter(cashSaleItem => {
        const cashSaleIdentifier = this.getCashSaleIdentifier(cashSaleItem);
        if (cashSaleCollectionIdentifiers.includes(cashSaleIdentifier)) {
          return false;
        }
        cashSaleCollectionIdentifiers.push(cashSaleIdentifier);
        return true;
      });
      return [...cashSalesToAdd, ...cashSaleCollection];
    }
    return cashSaleCollection;
  }

  protected convertDateFromClient<T extends ICashSale | NewCashSale | PartialUpdateCashSale>(cashSale: T): RestOf<T> {
    return {
      ...cashSale,
      saleDate: cashSale.saleDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCashSale: RestCashSale): ICashSale {
    return {
      ...restCashSale,
      saleDate: restCashSale.saleDate ? dayjs(restCashSale.saleDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCashSale>): HttpResponse<ICashSale> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCashSale[]>): HttpResponse<ICashSale[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
