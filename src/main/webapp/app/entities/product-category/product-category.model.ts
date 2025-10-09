export interface IProductCategory {
  id: number;
  name?: string | null;
}

export type NewProductCategory = Omit<IProductCategory, 'id'> & { id: null };
