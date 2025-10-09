import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FuelLogResolve from './route/fuel-log-routing-resolve.service';

const fuelLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fuel-log.component').then(m => m.FuelLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fuel-log-detail.component').then(m => m.FuelLogDetailComponent),
    resolve: {
      fuelLog: FuelLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fuel-log-update.component').then(m => m.FuelLogUpdateComponent),
    resolve: {
      fuelLog: FuelLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fuel-log-update.component').then(m => m.FuelLogUpdateComponent),
    resolve: {
      fuelLog: FuelLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fuelLogRoute;
