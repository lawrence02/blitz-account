import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ChartOfAccountResolve from './route/chart-of-account-routing-resolve.service';

const chartOfAccountRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/chart-of-account.component').then(m => m.ChartOfAccountComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/chart-of-account-detail.component').then(m => m.ChartOfAccountDetailComponent),
    resolve: {
      chartOfAccount: ChartOfAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/chart-of-account-update.component').then(m => m.ChartOfAccountUpdateComponent),
    resolve: {
      chartOfAccount: ChartOfAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/chart-of-account-update.component').then(m => m.ChartOfAccountUpdateComponent),
    resolve: {
      chartOfAccount: ChartOfAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default chartOfAccountRoute;
