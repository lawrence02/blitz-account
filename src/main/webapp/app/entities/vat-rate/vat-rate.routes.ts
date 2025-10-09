import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VATRateResolve from './route/vat-rate-routing-resolve.service';

const vATRateRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vat-rate.component').then(m => m.VATRateComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vat-rate-detail.component').then(m => m.VATRateDetailComponent),
    resolve: {
      vATRate: VATRateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vat-rate-update.component').then(m => m.VATRateUpdateComponent),
    resolve: {
      vATRate: VATRateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vat-rate-update.component').then(m => m.VATRateUpdateComponent),
    resolve: {
      vATRate: VATRateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vATRateRoute;
