import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BankTransactionResolve from './route/bank-transaction-routing-resolve.service';

const bankTransactionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bank-transaction.component').then(m => m.BankTransactionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bank-transaction-detail.component').then(m => m.BankTransactionDetailComponent),
    resolve: {
      bankTransaction: BankTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bank-transaction-update.component').then(m => m.BankTransactionUpdateComponent),
    resolve: {
      bankTransaction: BankTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bank-transaction-update.component').then(m => m.BankTransactionUpdateComponent),
    resolve: {
      bankTransaction: BankTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bankTransactionRoute;
