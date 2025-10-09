import dayjs from 'dayjs/esm';

export interface IBudget {
  id: number;
  name?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  accountId?: number | null;
  amount?: number | null;
}

export type NewBudget = Omit<IBudget, 'id'> & { id: null };
