import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuote, NewQuote } from '../quote.model';

export type PartialUpdateQuote = Partial<IQuote> & Pick<IQuote, 'id'>;

type RestOf<T extends IQuote | NewQuote> = Omit<T, 'issueDate'> & {
  issueDate?: string | null;
};

export type RestQuote = RestOf<IQuote>;

export type NewRestQuote = RestOf<NewQuote>;

export type PartialUpdateRestQuote = RestOf<PartialUpdateQuote>;

export type EntityResponseType = HttpResponse<IQuote>;
export type EntityArrayResponseType = HttpResponse<IQuote[]>;

@Injectable({ providedIn: 'root' })
export class QuoteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quotes');

  create(quote: NewQuote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quote);
    return this.http.post<RestQuote>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(quote: IQuote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quote);
    return this.http
      .put<RestQuote>(`${this.resourceUrl}/${this.getQuoteIdentifier(quote)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(quote: PartialUpdateQuote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quote);
    return this.http
      .patch<RestQuote>(`${this.resourceUrl}/${this.getQuoteIdentifier(quote)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestQuote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQuote[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuoteIdentifier(quote: Pick<IQuote, 'id'>): number {
    return quote.id;
  }

  compareQuote(o1: Pick<IQuote, 'id'> | null, o2: Pick<IQuote, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuoteIdentifier(o1) === this.getQuoteIdentifier(o2) : o1 === o2;
  }

  addQuoteToCollectionIfMissing<Type extends Pick<IQuote, 'id'>>(
    quoteCollection: Type[],
    ...quotesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quotes: Type[] = quotesToCheck.filter(isPresent);
    if (quotes.length > 0) {
      const quoteCollectionIdentifiers = quoteCollection.map(quoteItem => this.getQuoteIdentifier(quoteItem));
      const quotesToAdd = quotes.filter(quoteItem => {
        const quoteIdentifier = this.getQuoteIdentifier(quoteItem);
        if (quoteCollectionIdentifiers.includes(quoteIdentifier)) {
          return false;
        }
        quoteCollectionIdentifiers.push(quoteIdentifier);
        return true;
      });
      return [...quotesToAdd, ...quoteCollection];
    }
    return quoteCollection;
  }

  protected convertDateFromClient<T extends IQuote | NewQuote | PartialUpdateQuote>(quote: T): RestOf<T> {
    return {
      ...quote,
      issueDate: quote.issueDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restQuote: RestQuote): IQuote {
    return {
      ...restQuote,
      issueDate: restQuote.issueDate ? dayjs(restQuote.issueDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestQuote>): HttpResponse<IQuote> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestQuote[]>): HttpResponse<IQuote[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
