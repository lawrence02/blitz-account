import { AccountType } from 'app/entities/enumerations/account-type.model';

export interface IChartOfAccount {
  id: number;
  name?: string | null;
  accountType?: keyof typeof AccountType | null;
  code?: string | null;
  initialBalance?: number | null;
  currentBalance?: number | null;
}

export type NewChartOfAccount = Omit<IChartOfAccount, 'id'> & { id: null };
