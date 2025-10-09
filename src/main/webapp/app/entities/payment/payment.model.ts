import dayjs from 'dayjs/esm';
import { PaymentMethod } from 'app/entities/enumerations/payment-method.model';

export interface IPayment {
  id: number;
  amount?: number | null;
  paymentDate?: dayjs.Dayjs | null;
  method?: keyof typeof PaymentMethod | null;
  currencyId?: number | null;
  bankAccountId?: number | null;
  invoiceId?: number | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
