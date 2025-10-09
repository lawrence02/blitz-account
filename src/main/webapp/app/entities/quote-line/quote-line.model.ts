export interface IQuoteLine {
  id: number;
  quoteId?: number | null;
  productId?: number | null;
  quantity?: number | null;
  unitPrice?: number | null;
  vatRateId?: number | null;
}

export type NewQuoteLine = Omit<IQuoteLine, 'id'> & { id: null };
