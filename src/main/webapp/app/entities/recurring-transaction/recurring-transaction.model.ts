import dayjs from 'dayjs/esm';

export interface IRecurringTransaction {
  id: number;
  name?: string | null;
  amount?: number | null;
  frequency?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  accountId?: number | null;
  currencyId?: number | null;
  vatRateId?: number | null;
}

export type NewRecurringTransaction = Omit<IRecurringTransaction, 'id'> & { id: null };
