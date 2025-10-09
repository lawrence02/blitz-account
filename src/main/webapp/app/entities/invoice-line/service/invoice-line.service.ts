import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoiceLine, NewInvoiceLine } from '../invoice-line.model';

export type PartialUpdateInvoiceLine = Partial<IInvoiceLine> & Pick<IInvoiceLine, 'id'>;

export type EntityResponseType = HttpResponse<IInvoiceLine>;
export type EntityArrayResponseType = HttpResponse<IInvoiceLine[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceLineService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoice-lines');

  create(invoiceLine: NewInvoiceLine): Observable<EntityResponseType> {
    return this.http.post<IInvoiceLine>(this.resourceUrl, invoiceLine, { observe: 'response' });
  }

  update(invoiceLine: IInvoiceLine): Observable<EntityResponseType> {
    return this.http.put<IInvoiceLine>(`${this.resourceUrl}/${this.getInvoiceLineIdentifier(invoiceLine)}`, invoiceLine, {
      observe: 'response',
    });
  }

  partialUpdate(invoiceLine: PartialUpdateInvoiceLine): Observable<EntityResponseType> {
    return this.http.patch<IInvoiceLine>(`${this.resourceUrl}/${this.getInvoiceLineIdentifier(invoiceLine)}`, invoiceLine, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInvoiceLine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInvoiceLine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInvoiceLineIdentifier(invoiceLine: Pick<IInvoiceLine, 'id'>): number {
    return invoiceLine.id;
  }

  compareInvoiceLine(o1: Pick<IInvoiceLine, 'id'> | null, o2: Pick<IInvoiceLine, 'id'> | null): boolean {
    return o1 && o2 ? this.getInvoiceLineIdentifier(o1) === this.getInvoiceLineIdentifier(o2) : o1 === o2;
  }

  addInvoiceLineToCollectionIfMissing<Type extends Pick<IInvoiceLine, 'id'>>(
    invoiceLineCollection: Type[],
    ...invoiceLinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoiceLines: Type[] = invoiceLinesToCheck.filter(isPresent);
    if (invoiceLines.length > 0) {
      const invoiceLineCollectionIdentifiers = invoiceLineCollection.map(invoiceLineItem => this.getInvoiceLineIdentifier(invoiceLineItem));
      const invoiceLinesToAdd = invoiceLines.filter(invoiceLineItem => {
        const invoiceLineIdentifier = this.getInvoiceLineIdentifier(invoiceLineItem);
        if (invoiceLineCollectionIdentifiers.includes(invoiceLineIdentifier)) {
          return false;
        }
        invoiceLineCollectionIdentifiers.push(invoiceLineIdentifier);
        return true;
      });
      return [...invoiceLinesToAdd, ...invoiceLineCollection];
    }
    return invoiceLineCollection;
  }
}
