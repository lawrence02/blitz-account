export interface IBankAccount {
  id: number;
  name?: string | null;
  accountNumber?: string | null;
  bankName?: string | null;
}

export type NewBankAccount = Omit<IBankAccount, 'id'> & { id: null };
