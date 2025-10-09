import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBudget } from '../budget.model';
import { BudgetService } from '../service/budget.service';
import { BudgetFormGroup, BudgetFormService } from './budget-form.service';

@Component({
  selector: 'jhi-budget-update',
  templateUrl: './budget-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BudgetUpdateComponent implements OnInit {
  isSaving = false;
  budget: IBudget | null = null;

  protected budgetService = inject(BudgetService);
  protected budgetFormService = inject(BudgetFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BudgetFormGroup = this.budgetFormService.createBudgetFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ budget }) => {
      this.budget = budget;
      if (budget) {
        this.updateForm(budget);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const budget = this.budgetFormService.getBudget(this.editForm);
    if (budget.id !== null) {
      this.subscribeToSaveResponse(this.budgetService.update(budget));
    } else {
      this.subscribeToSaveResponse(this.budgetService.create(budget));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBudget>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(budget: IBudget): void {
    this.budget = budget;
    this.budgetFormService.resetForm(this.editForm, budget);
  }
}
