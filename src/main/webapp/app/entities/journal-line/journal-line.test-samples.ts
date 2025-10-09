import { IJournalLine, NewJournalLine } from './journal-line.model';

export const sampleWithRequiredData: IJournalLine = {
  id: 31389,
  journalId: 18181,
  accountId: 19558,
};

export const sampleWithPartialData: IJournalLine = {
  id: 5604,
  journalId: 13435,
  accountId: 6814,
  credit: 13130.21,
};

export const sampleWithFullData: IJournalLine = {
  id: 31401,
  journalId: 356,
  accountId: 6267,
  debit: 28598.91,
  credit: 22185.85,
};

export const sampleWithNewData: NewJournalLine = {
  journalId: 6532,
  accountId: 28745,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
