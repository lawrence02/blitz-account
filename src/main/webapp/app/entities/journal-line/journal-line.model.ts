export interface IJournalLine {
  id: number;
  journalId?: number | null;
  accountId?: number | null;
  debit?: number | null;
  credit?: number | null;
}

export type NewJournalLine = Omit<IJournalLine, 'id'> & { id: null };
