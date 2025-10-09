import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RecurringTransactionResolve from './route/recurring-transaction-routing-resolve.service';

const recurringTransactionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/recurring-transaction.component').then(m => m.RecurringTransactionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/recurring-transaction-detail.component').then(m => m.RecurringTransactionDetailComponent),
    resolve: {
      recurringTransaction: RecurringTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/recurring-transaction-update.component').then(m => m.RecurringTransactionUpdateComponent),
    resolve: {
      recurringTransaction: RecurringTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/recurring-transaction-update.component').then(m => m.RecurringTransactionUpdateComponent),
    resolve: {
      recurringTransaction: RecurringTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default recurringTransactionRoute;
