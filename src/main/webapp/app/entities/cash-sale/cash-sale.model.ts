import dayjs from 'dayjs/esm';

export interface ICashSale {
  id: number;
  productId?: number | null;
  quantity?: number | null;
  unitPrice?: number | null;
  vatRateId?: number | null;
  currencyId?: number | null;
  saleDate?: dayjs.Dayjs | null;
}

export type NewCashSale = Omit<ICashSale, 'id'> & { id: null };
