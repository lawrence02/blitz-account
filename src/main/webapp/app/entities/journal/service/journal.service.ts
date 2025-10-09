import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJournal, NewJournal } from '../journal.model';

export type PartialUpdateJournal = Partial<IJournal> & Pick<IJournal, 'id'>;

type RestOf<T extends IJournal | NewJournal> = Omit<T, 'journalDate'> & {
  journalDate?: string | null;
};

export type RestJournal = RestOf<IJournal>;

export type NewRestJournal = RestOf<NewJournal>;

export type PartialUpdateRestJournal = RestOf<PartialUpdateJournal>;

export type EntityResponseType = HttpResponse<IJournal>;
export type EntityArrayResponseType = HttpResponse<IJournal[]>;

@Injectable({ providedIn: 'root' })
export class JournalService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/journals');

  create(journal: NewJournal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(journal);
    return this.http
      .post<RestJournal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(journal: IJournal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(journal);
    return this.http
      .put<RestJournal>(`${this.resourceUrl}/${this.getJournalIdentifier(journal)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(journal: PartialUpdateJournal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(journal);
    return this.http
      .patch<RestJournal>(`${this.resourceUrl}/${this.getJournalIdentifier(journal)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestJournal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestJournal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getJournalIdentifier(journal: Pick<IJournal, 'id'>): number {
    return journal.id;
  }

  compareJournal(o1: Pick<IJournal, 'id'> | null, o2: Pick<IJournal, 'id'> | null): boolean {
    return o1 && o2 ? this.getJournalIdentifier(o1) === this.getJournalIdentifier(o2) : o1 === o2;
  }

  addJournalToCollectionIfMissing<Type extends Pick<IJournal, 'id'>>(
    journalCollection: Type[],
    ...journalsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const journals: Type[] = journalsToCheck.filter(isPresent);
    if (journals.length > 0) {
      const journalCollectionIdentifiers = journalCollection.map(journalItem => this.getJournalIdentifier(journalItem));
      const journalsToAdd = journals.filter(journalItem => {
        const journalIdentifier = this.getJournalIdentifier(journalItem);
        if (journalCollectionIdentifiers.includes(journalIdentifier)) {
          return false;
        }
        journalCollectionIdentifiers.push(journalIdentifier);
        return true;
      });
      return [...journalsToAdd, ...journalCollection];
    }
    return journalCollection;
  }

  protected convertDateFromClient<T extends IJournal | NewJournal | PartialUpdateJournal>(journal: T): RestOf<T> {
    return {
      ...journal,
      journalDate: journal.journalDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restJournal: RestJournal): IJournal {
    return {
      ...restJournal,
      journalDate: restJournal.journalDate ? dayjs(restJournal.journalDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestJournal>): HttpResponse<IJournal> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestJournal[]>): HttpResponse<IJournal[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
