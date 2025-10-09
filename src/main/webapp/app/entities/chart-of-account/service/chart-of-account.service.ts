import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChartOfAccount, NewChartOfAccount } from '../chart-of-account.model';

export type PartialUpdateChartOfAccount = Partial<IChartOfAccount> & Pick<IChartOfAccount, 'id'>;

export type EntityResponseType = HttpResponse<IChartOfAccount>;
export type EntityArrayResponseType = HttpResponse<IChartOfAccount[]>;

@Injectable({ providedIn: 'root' })
export class ChartOfAccountService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chart-of-accounts');

  create(chartOfAccount: NewChartOfAccount): Observable<EntityResponseType> {
    return this.http.post<IChartOfAccount>(this.resourceUrl, chartOfAccount, { observe: 'response' });
  }

  update(chartOfAccount: IChartOfAccount): Observable<EntityResponseType> {
    return this.http.put<IChartOfAccount>(`${this.resourceUrl}/${this.getChartOfAccountIdentifier(chartOfAccount)}`, chartOfAccount, {
      observe: 'response',
    });
  }

  partialUpdate(chartOfAccount: PartialUpdateChartOfAccount): Observable<EntityResponseType> {
    return this.http.patch<IChartOfAccount>(`${this.resourceUrl}/${this.getChartOfAccountIdentifier(chartOfAccount)}`, chartOfAccount, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChartOfAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChartOfAccount[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getChartOfAccountIdentifier(chartOfAccount: Pick<IChartOfAccount, 'id'>): number {
    return chartOfAccount.id;
  }

  compareChartOfAccount(o1: Pick<IChartOfAccount, 'id'> | null, o2: Pick<IChartOfAccount, 'id'> | null): boolean {
    return o1 && o2 ? this.getChartOfAccountIdentifier(o1) === this.getChartOfAccountIdentifier(o2) : o1 === o2;
  }

  addChartOfAccountToCollectionIfMissing<Type extends Pick<IChartOfAccount, 'id'>>(
    chartOfAccountCollection: Type[],
    ...chartOfAccountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const chartOfAccounts: Type[] = chartOfAccountsToCheck.filter(isPresent);
    if (chartOfAccounts.length > 0) {
      const chartOfAccountCollectionIdentifiers = chartOfAccountCollection.map(chartOfAccountItem =>
        this.getChartOfAccountIdentifier(chartOfAccountItem),
      );
      const chartOfAccountsToAdd = chartOfAccounts.filter(chartOfAccountItem => {
        const chartOfAccountIdentifier = this.getChartOfAccountIdentifier(chartOfAccountItem);
        if (chartOfAccountCollectionIdentifiers.includes(chartOfAccountIdentifier)) {
          return false;
        }
        chartOfAccountCollectionIdentifiers.push(chartOfAccountIdentifier);
        return true;
      });
      return [...chartOfAccountsToAdd, ...chartOfAccountCollection];
    }
    return chartOfAccountCollection;
  }
}
