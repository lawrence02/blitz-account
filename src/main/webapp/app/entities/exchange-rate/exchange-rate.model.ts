import dayjs from 'dayjs/esm';

export interface IExchangeRate {
  id: number;
  baseCurrencyId?: number | null;
  targetCurrencyId?: number | null;
  rate?: number | null;
  rateDate?: dayjs.Dayjs | null;
}

export type NewExchangeRate = Omit<IExchangeRate, 'id'> & { id: null };
