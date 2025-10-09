import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJournalLine, NewJournalLine } from '../journal-line.model';

export type PartialUpdateJournalLine = Partial<IJournalLine> & Pick<IJournalLine, 'id'>;

export type EntityResponseType = HttpResponse<IJournalLine>;
export type EntityArrayResponseType = HttpResponse<IJournalLine[]>;

@Injectable({ providedIn: 'root' })
export class JournalLineService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/journal-lines');

  create(journalLine: NewJournalLine): Observable<EntityResponseType> {
    return this.http.post<IJournalLine>(this.resourceUrl, journalLine, { observe: 'response' });
  }

  update(journalLine: IJournalLine): Observable<EntityResponseType> {
    return this.http.put<IJournalLine>(`${this.resourceUrl}/${this.getJournalLineIdentifier(journalLine)}`, journalLine, {
      observe: 'response',
    });
  }

  partialUpdate(journalLine: PartialUpdateJournalLine): Observable<EntityResponseType> {
    return this.http.patch<IJournalLine>(`${this.resourceUrl}/${this.getJournalLineIdentifier(journalLine)}`, journalLine, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJournalLine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJournalLine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getJournalLineIdentifier(journalLine: Pick<IJournalLine, 'id'>): number {
    return journalLine.id;
  }

  compareJournalLine(o1: Pick<IJournalLine, 'id'> | null, o2: Pick<IJournalLine, 'id'> | null): boolean {
    return o1 && o2 ? this.getJournalLineIdentifier(o1) === this.getJournalLineIdentifier(o2) : o1 === o2;
  }

  addJournalLineToCollectionIfMissing<Type extends Pick<IJournalLine, 'id'>>(
    journalLineCollection: Type[],
    ...journalLinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const journalLines: Type[] = journalLinesToCheck.filter(isPresent);
    if (journalLines.length > 0) {
      const journalLineCollectionIdentifiers = journalLineCollection.map(journalLineItem => this.getJournalLineIdentifier(journalLineItem));
      const journalLinesToAdd = journalLines.filter(journalLineItem => {
        const journalLineIdentifier = this.getJournalLineIdentifier(journalLineItem);
        if (journalLineCollectionIdentifiers.includes(journalLineIdentifier)) {
          return false;
        }
        journalLineCollectionIdentifiers.push(journalLineIdentifier);
        return true;
      });
      return [...journalLinesToAdd, ...journalLineCollection];
    }
    return journalLineCollection;
  }
}
