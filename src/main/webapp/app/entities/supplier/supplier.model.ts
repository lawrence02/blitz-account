export interface ISupplier {
  id: number;
  name?: string | null;
  contactNumber?: string | null;
  email?: string | null;
  address?: string | null;
}

export type NewSupplier = Omit<ISupplier, 'id'> & { id: null };
