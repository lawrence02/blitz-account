import dayjs from 'dayjs/esm';

export interface IJournal {
  id: number;
  journalDate?: dayjs.Dayjs | null;
  reference?: string | null;
  description?: string | null;
}

export type NewJournal = Omit<IJournal, 'id'> & { id: null };
