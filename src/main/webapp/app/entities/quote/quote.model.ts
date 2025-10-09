import dayjs from 'dayjs/esm';
import { DocumentStatus } from 'app/entities/enumerations/document-status.model';

export interface IQuote {
  id: number;
  clientName?: string | null;
  issueDate?: dayjs.Dayjs | null;
  status?: keyof typeof DocumentStatus | null;
  currencyId?: number | null;
  vatRateId?: number | null;
  totalAmount?: number | null;
}

export type NewQuote = Omit<IQuote, 'id'> & { id: null };
