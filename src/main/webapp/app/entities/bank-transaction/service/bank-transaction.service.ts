import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBankTransaction, NewBankTransaction } from '../bank-transaction.model';

export type PartialUpdateBankTransaction = Partial<IBankTransaction> & Pick<IBankTransaction, 'id'>;

type RestOf<T extends IBankTransaction | NewBankTransaction> = Omit<T, 'transactionDate'> & {
  transactionDate?: string | null;
};

export type RestBankTransaction = RestOf<IBankTransaction>;

export type NewRestBankTransaction = RestOf<NewBankTransaction>;

export type PartialUpdateRestBankTransaction = RestOf<PartialUpdateBankTransaction>;

export type EntityResponseType = HttpResponse<IBankTransaction>;
export type EntityArrayResponseType = HttpResponse<IBankTransaction[]>;

@Injectable({ providedIn: 'root' })
export class BankTransactionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bank-transactions');

  create(bankTransaction: NewBankTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankTransaction);
    return this.http
      .post<RestBankTransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bankTransaction: IBankTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankTransaction);
    return this.http
      .put<RestBankTransaction>(`${this.resourceUrl}/${this.getBankTransactionIdentifier(bankTransaction)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bankTransaction: PartialUpdateBankTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankTransaction);
    return this.http
      .patch<RestBankTransaction>(`${this.resourceUrl}/${this.getBankTransactionIdentifier(bankTransaction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBankTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBankTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBankTransactionIdentifier(bankTransaction: Pick<IBankTransaction, 'id'>): number {
    return bankTransaction.id;
  }

  compareBankTransaction(o1: Pick<IBankTransaction, 'id'> | null, o2: Pick<IBankTransaction, 'id'> | null): boolean {
    return o1 && o2 ? this.getBankTransactionIdentifier(o1) === this.getBankTransactionIdentifier(o2) : o1 === o2;
  }

  addBankTransactionToCollectionIfMissing<Type extends Pick<IBankTransaction, 'id'>>(
    bankTransactionCollection: Type[],
    ...bankTransactionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bankTransactions: Type[] = bankTransactionsToCheck.filter(isPresent);
    if (bankTransactions.length > 0) {
      const bankTransactionCollectionIdentifiers = bankTransactionCollection.map(bankTransactionItem =>
        this.getBankTransactionIdentifier(bankTransactionItem),
      );
      const bankTransactionsToAdd = bankTransactions.filter(bankTransactionItem => {
        const bankTransactionIdentifier = this.getBankTransactionIdentifier(bankTransactionItem);
        if (bankTransactionCollectionIdentifiers.includes(bankTransactionIdentifier)) {
          return false;
        }
        bankTransactionCollectionIdentifiers.push(bankTransactionIdentifier);
        return true;
      });
      return [...bankTransactionsToAdd, ...bankTransactionCollection];
    }
    return bankTransactionCollection;
  }

  protected convertDateFromClient<T extends IBankTransaction | NewBankTransaction | PartialUpdateBankTransaction>(
    bankTransaction: T,
  ): RestOf<T> {
    return {
      ...bankTransaction,
      transactionDate: bankTransaction.transactionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBankTransaction: RestBankTransaction): IBankTransaction {
    return {
      ...restBankTransaction,
      transactionDate: restBankTransaction.transactionDate ? dayjs(restBankTransaction.transactionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBankTransaction>): HttpResponse<IBankTransaction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBankTransaction[]>): HttpResponse<IBankTransaction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
