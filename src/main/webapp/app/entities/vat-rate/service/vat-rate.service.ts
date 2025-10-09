import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVATRate, NewVATRate } from '../vat-rate.model';

export type PartialUpdateVATRate = Partial<IVATRate> & Pick<IVATRate, 'id'>;

export type EntityResponseType = HttpResponse<IVATRate>;
export type EntityArrayResponseType = HttpResponse<IVATRate[]>;

@Injectable({ providedIn: 'root' })
export class VATRateService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vat-rates');

  create(vATRate: NewVATRate): Observable<EntityResponseType> {
    return this.http.post<IVATRate>(this.resourceUrl, vATRate, { observe: 'response' });
  }

  update(vATRate: IVATRate): Observable<EntityResponseType> {
    return this.http.put<IVATRate>(`${this.resourceUrl}/${this.getVATRateIdentifier(vATRate)}`, vATRate, { observe: 'response' });
  }

  partialUpdate(vATRate: PartialUpdateVATRate): Observable<EntityResponseType> {
    return this.http.patch<IVATRate>(`${this.resourceUrl}/${this.getVATRateIdentifier(vATRate)}`, vATRate, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVATRate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVATRate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVATRateIdentifier(vATRate: Pick<IVATRate, 'id'>): number {
    return vATRate.id;
  }

  compareVATRate(o1: Pick<IVATRate, 'id'> | null, o2: Pick<IVATRate, 'id'> | null): boolean {
    return o1 && o2 ? this.getVATRateIdentifier(o1) === this.getVATRateIdentifier(o2) : o1 === o2;
  }

  addVATRateToCollectionIfMissing<Type extends Pick<IVATRate, 'id'>>(
    vATRateCollection: Type[],
    ...vATRatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vATRates: Type[] = vATRatesToCheck.filter(isPresent);
    if (vATRates.length > 0) {
      const vATRateCollectionIdentifiers = vATRateCollection.map(vATRateItem => this.getVATRateIdentifier(vATRateItem));
      const vATRatesToAdd = vATRates.filter(vATRateItem => {
        const vATRateIdentifier = this.getVATRateIdentifier(vATRateItem);
        if (vATRateCollectionIdentifiers.includes(vATRateIdentifier)) {
          return false;
        }
        vATRateCollectionIdentifiers.push(vATRateIdentifier);
        return true;
      });
      return [...vATRatesToAdd, ...vATRateCollection];
    }
    return vATRateCollection;
  }
}
