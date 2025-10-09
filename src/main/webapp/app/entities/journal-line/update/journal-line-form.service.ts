import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IJournalLine, NewJournalLine } from '../journal-line.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJournalLine for edit and NewJournalLineFormGroupInput for create.
 */
type JournalLineFormGroupInput = IJournalLine | PartialWithRequiredKeyOf<NewJournalLine>;

type JournalLineFormDefaults = Pick<NewJournalLine, 'id'>;

type JournalLineFormGroupContent = {
  id: FormControl<IJournalLine['id'] | NewJournalLine['id']>;
  journalId: FormControl<IJournalLine['journalId']>;
  accountId: FormControl<IJournalLine['accountId']>;
  debit: FormControl<IJournalLine['debit']>;
  credit: FormControl<IJournalLine['credit']>;
};

export type JournalLineFormGroup = FormGroup<JournalLineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JournalLineFormService {
  createJournalLineFormGroup(journalLine: JournalLineFormGroupInput = { id: null }): JournalLineFormGroup {
    const journalLineRawValue = {
      ...this.getFormDefaults(),
      ...journalLine,
    };
    return new FormGroup<JournalLineFormGroupContent>({
      id: new FormControl(
        { value: journalLineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      journalId: new FormControl(journalLineRawValue.journalId, {
        validators: [Validators.required],
      }),
      accountId: new FormControl(journalLineRawValue.accountId, {
        validators: [Validators.required],
      }),
      debit: new FormControl(journalLineRawValue.debit),
      credit: new FormControl(journalLineRawValue.credit),
    });
  }

  getJournalLine(form: JournalLineFormGroup): IJournalLine | NewJournalLine {
    return form.getRawValue() as IJournalLine | NewJournalLine;
  }

  resetForm(form: JournalLineFormGroup, journalLine: JournalLineFormGroupInput): void {
    const journalLineRawValue = { ...this.getFormDefaults(), ...journalLine };
    form.reset(
      {
        ...journalLineRawValue,
        id: { value: journalLineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): JournalLineFormDefaults {
    return {
      id: null,
    };
  }
}
