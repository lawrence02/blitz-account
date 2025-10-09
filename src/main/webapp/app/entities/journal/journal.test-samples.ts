import dayjs from 'dayjs/esm';

import { IJournal, NewJournal } from './journal.model';

export const sampleWithRequiredData: IJournal = {
  id: 21122,
  journalDate: dayjs('2025-10-08T09:45'),
};

export const sampleWithPartialData: IJournal = {
  id: 25243,
  journalDate: dayjs('2025-10-08T22:40'),
  reference: 'pushy',
};

export const sampleWithFullData: IJournal = {
  id: 19626,
  journalDate: dayjs('2025-10-08T09:20'),
  reference: 'indeed sleepily anti',
  description: 'apropos past linear',
};

export const sampleWithNewData: NewJournal = {
  journalDate: dayjs('2025-10-09T00:35'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
