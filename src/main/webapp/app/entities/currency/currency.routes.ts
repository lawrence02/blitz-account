import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CurrencyResolve from './route/currency-routing-resolve.service';

const currencyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/currency.component').then(m => m.CurrencyComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/currency-detail.component').then(m => m.CurrencyDetailComponent),
    resolve: {
      currency: CurrencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/currency-update.component').then(m => m.CurrencyUpdateComponent),
    resolve: {
      currency: CurrencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/currency-update.component').then(m => m.CurrencyUpdateComponent),
    resolve: {
      currency: CurrencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default currencyRoute;
