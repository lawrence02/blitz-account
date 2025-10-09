export interface IVATRate {
  id: number;
  name?: string | null;
  percentage?: number | null;
}

export type NewVATRate = Omit<IVATRate, 'id'> & { id: null };
