import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuoteLine, NewQuoteLine } from '../quote-line.model';

export type PartialUpdateQuoteLine = Partial<IQuoteLine> & Pick<IQuoteLine, 'id'>;

export type EntityResponseType = HttpResponse<IQuoteLine>;
export type EntityArrayResponseType = HttpResponse<IQuoteLine[]>;

@Injectable({ providedIn: 'root' })
export class QuoteLineService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quote-lines');

  create(quoteLine: NewQuoteLine): Observable<EntityResponseType> {
    return this.http.post<IQuoteLine>(this.resourceUrl, quoteLine, { observe: 'response' });
  }

  update(quoteLine: IQuoteLine): Observable<EntityResponseType> {
    return this.http.put<IQuoteLine>(`${this.resourceUrl}/${this.getQuoteLineIdentifier(quoteLine)}`, quoteLine, { observe: 'response' });
  }

  partialUpdate(quoteLine: PartialUpdateQuoteLine): Observable<EntityResponseType> {
    return this.http.patch<IQuoteLine>(`${this.resourceUrl}/${this.getQuoteLineIdentifier(quoteLine)}`, quoteLine, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuoteLine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuoteLine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuoteLineIdentifier(quoteLine: Pick<IQuoteLine, 'id'>): number {
    return quoteLine.id;
  }

  compareQuoteLine(o1: Pick<IQuoteLine, 'id'> | null, o2: Pick<IQuoteLine, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuoteLineIdentifier(o1) === this.getQuoteLineIdentifier(o2) : o1 === o2;
  }

  addQuoteLineToCollectionIfMissing<Type extends Pick<IQuoteLine, 'id'>>(
    quoteLineCollection: Type[],
    ...quoteLinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quoteLines: Type[] = quoteLinesToCheck.filter(isPresent);
    if (quoteLines.length > 0) {
      const quoteLineCollectionIdentifiers = quoteLineCollection.map(quoteLineItem => this.getQuoteLineIdentifier(quoteLineItem));
      const quoteLinesToAdd = quoteLines.filter(quoteLineItem => {
        const quoteLineIdentifier = this.getQuoteLineIdentifier(quoteLineItem);
        if (quoteLineCollectionIdentifiers.includes(quoteLineIdentifier)) {
          return false;
        }
        quoteLineCollectionIdentifiers.push(quoteLineIdentifier);
        return true;
      });
      return [...quoteLinesToAdd, ...quoteLineCollection];
    }
    return quoteLineCollection;
  }
}
