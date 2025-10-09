import dayjs from 'dayjs/esm';
import { TransactionDirection } from 'app/entities/enumerations/transaction-direction.model';

export interface IBankTransaction {
  id: number;
  bankAccountId?: number | null;
  transactionDate?: dayjs.Dayjs | null;
  reference?: string | null;
  amount?: number | null;
  direction?: keyof typeof TransactionDirection | null;
  relatedPaymentId?: number | null;
  description?: string | null;
}

export type NewBankTransaction = Omit<IBankTransaction, 'id'> & { id: null };
