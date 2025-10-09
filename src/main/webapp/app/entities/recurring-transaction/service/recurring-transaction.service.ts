import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecurringTransaction, NewRecurringTransaction } from '../recurring-transaction.model';

export type PartialUpdateRecurringTransaction = Partial<IRecurringTransaction> & Pick<IRecurringTransaction, 'id'>;

type RestOf<T extends IRecurringTransaction | NewRecurringTransaction> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestRecurringTransaction = RestOf<IRecurringTransaction>;

export type NewRestRecurringTransaction = RestOf<NewRecurringTransaction>;

export type PartialUpdateRestRecurringTransaction = RestOf<PartialUpdateRecurringTransaction>;

export type EntityResponseType = HttpResponse<IRecurringTransaction>;
export type EntityArrayResponseType = HttpResponse<IRecurringTransaction[]>;

@Injectable({ providedIn: 'root' })
export class RecurringTransactionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recurring-transactions');

  create(recurringTransaction: NewRecurringTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recurringTransaction);
    return this.http
      .post<RestRecurringTransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(recurringTransaction: IRecurringTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recurringTransaction);
    return this.http
      .put<RestRecurringTransaction>(`${this.resourceUrl}/${this.getRecurringTransactionIdentifier(recurringTransaction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(recurringTransaction: PartialUpdateRecurringTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recurringTransaction);
    return this.http
      .patch<RestRecurringTransaction>(`${this.resourceUrl}/${this.getRecurringTransactionIdentifier(recurringTransaction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRecurringTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRecurringTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRecurringTransactionIdentifier(recurringTransaction: Pick<IRecurringTransaction, 'id'>): number {
    return recurringTransaction.id;
  }

  compareRecurringTransaction(o1: Pick<IRecurringTransaction, 'id'> | null, o2: Pick<IRecurringTransaction, 'id'> | null): boolean {
    return o1 && o2 ? this.getRecurringTransactionIdentifier(o1) === this.getRecurringTransactionIdentifier(o2) : o1 === o2;
  }

  addRecurringTransactionToCollectionIfMissing<Type extends Pick<IRecurringTransaction, 'id'>>(
    recurringTransactionCollection: Type[],
    ...recurringTransactionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const recurringTransactions: Type[] = recurringTransactionsToCheck.filter(isPresent);
    if (recurringTransactions.length > 0) {
      const recurringTransactionCollectionIdentifiers = recurringTransactionCollection.map(recurringTransactionItem =>
        this.getRecurringTransactionIdentifier(recurringTransactionItem),
      );
      const recurringTransactionsToAdd = recurringTransactions.filter(recurringTransactionItem => {
        const recurringTransactionIdentifier = this.getRecurringTransactionIdentifier(recurringTransactionItem);
        if (recurringTransactionCollectionIdentifiers.includes(recurringTransactionIdentifier)) {
          return false;
        }
        recurringTransactionCollectionIdentifiers.push(recurringTransactionIdentifier);
        return true;
      });
      return [...recurringTransactionsToAdd, ...recurringTransactionCollection];
    }
    return recurringTransactionCollection;
  }

  protected convertDateFromClient<T extends IRecurringTransaction | NewRecurringTransaction | PartialUpdateRecurringTransaction>(
    recurringTransaction: T,
  ): RestOf<T> {
    return {
      ...recurringTransaction,
      startDate: recurringTransaction.startDate?.toJSON() ?? null,
      endDate: recurringTransaction.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRecurringTransaction: RestRecurringTransaction): IRecurringTransaction {
    return {
      ...restRecurringTransaction,
      startDate: restRecurringTransaction.startDate ? dayjs(restRecurringTransaction.startDate) : undefined,
      endDate: restRecurringTransaction.endDate ? dayjs(restRecurringTransaction.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRecurringTransaction>): HttpResponse<IRecurringTransaction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRecurringTransaction[]>): HttpResponse<IRecurringTransaction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
