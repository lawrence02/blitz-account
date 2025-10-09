import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IJournal, NewJournal } from '../journal.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJournal for edit and NewJournalFormGroupInput for create.
 */
type JournalFormGroupInput = IJournal | PartialWithRequiredKeyOf<NewJournal>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IJournal | NewJournal> = Omit<T, 'journalDate'> & {
  journalDate?: string | null;
};

type JournalFormRawValue = FormValueOf<IJournal>;

type NewJournalFormRawValue = FormValueOf<NewJournal>;

type JournalFormDefaults = Pick<NewJournal, 'id' | 'journalDate'>;

type JournalFormGroupContent = {
  id: FormControl<JournalFormRawValue['id'] | NewJournal['id']>;
  journalDate: FormControl<JournalFormRawValue['journalDate']>;
  reference: FormControl<JournalFormRawValue['reference']>;
  description: FormControl<JournalFormRawValue['description']>;
};

export type JournalFormGroup = FormGroup<JournalFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JournalFormService {
  createJournalFormGroup(journal: JournalFormGroupInput = { id: null }): JournalFormGroup {
    const journalRawValue = this.convertJournalToJournalRawValue({
      ...this.getFormDefaults(),
      ...journal,
    });
    return new FormGroup<JournalFormGroupContent>({
      id: new FormControl(
        { value: journalRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      journalDate: new FormControl(journalRawValue.journalDate, {
        validators: [Validators.required],
      }),
      reference: new FormControl(journalRawValue.reference),
      description: new FormControl(journalRawValue.description),
    });
  }

  getJournal(form: JournalFormGroup): IJournal | NewJournal {
    return this.convertJournalRawValueToJournal(form.getRawValue() as JournalFormRawValue | NewJournalFormRawValue);
  }

  resetForm(form: JournalFormGroup, journal: JournalFormGroupInput): void {
    const journalRawValue = this.convertJournalToJournalRawValue({ ...this.getFormDefaults(), ...journal });
    form.reset(
      {
        ...journalRawValue,
        id: { value: journalRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): JournalFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      journalDate: currentTime,
    };
  }

  private convertJournalRawValueToJournal(rawJournal: JournalFormRawValue | NewJournalFormRawValue): IJournal | NewJournal {
    return {
      ...rawJournal,
      journalDate: dayjs(rawJournal.journalDate, DATE_TIME_FORMAT),
    };
  }

  private convertJournalToJournalRawValue(
    journal: IJournal | (Partial<NewJournal> & JournalFormDefaults),
  ): JournalFormRawValue | PartialWithRequiredKeyOf<NewJournalFormRawValue> {
    return {
      ...journal,
      journalDate: journal.journalDate ? journal.journalDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
