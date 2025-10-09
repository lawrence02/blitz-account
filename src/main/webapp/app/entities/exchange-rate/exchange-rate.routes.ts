import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExchangeRateResolve from './route/exchange-rate-routing-resolve.service';

const exchangeRateRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/exchange-rate.component').then(m => m.ExchangeRateComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/exchange-rate-detail.component').then(m => m.ExchangeRateDetailComponent),
    resolve: {
      exchangeRate: ExchangeRateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/exchange-rate-update.component').then(m => m.ExchangeRateUpdateComponent),
    resolve: {
      exchangeRate: ExchangeRateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/exchange-rate-update.component').then(m => m.ExchangeRateUpdateComponent),
    resolve: {
      exchangeRate: ExchangeRateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default exchangeRateRoute;
