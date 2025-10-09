export interface IInvoiceLine {
  id: number;
  invoiceId?: number | null;
  productId?: number | null;
  quantity?: number | null;
  unitPrice?: number | null;
  vatRateId?: number | null;
}

export type NewInvoiceLine = Omit<IInvoiceLine, 'id'> & { id: null };
