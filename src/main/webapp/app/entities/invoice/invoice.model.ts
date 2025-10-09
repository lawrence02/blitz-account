import dayjs from 'dayjs/esm';
import { DocumentStatus } from 'app/entities/enumerations/document-status.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

export interface IInvoice {
  id: number;
  clientName?: string | null;
  issueDate?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  status?: keyof typeof DocumentStatus | null;
  currencyId?: number | null;
  vatRateId?: number | null;
  totalAmount?: number | null;
  paidAmount?: number | null;
  paymentStatus?: keyof typeof PaymentStatus | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
