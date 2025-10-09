export interface IProduct {
  id: number;
  name?: string | null;
  categoryId?: number | null;
  supplierId?: number | null;
  unitCost?: number | null;
  unitPrice?: number | null;
  stockQty?: number | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
