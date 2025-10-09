import dayjs from 'dayjs/esm';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';

export interface ITransaction {
  id: number;
  type?: keyof typeof TransactionType | null;
  amount?: number | null;
  transactionDate?: dayjs.Dayjs | null;
  accountId?: number | null;
  vatRateId?: number | null;
  currencyId?: number | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
