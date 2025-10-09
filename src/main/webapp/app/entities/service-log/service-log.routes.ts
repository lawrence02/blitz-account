import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ServiceLogResolve from './route/service-log-routing-resolve.service';

const serviceLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/service-log.component').then(m => m.ServiceLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/service-log-detail.component').then(m => m.ServiceLogDetailComponent),
    resolve: {
      serviceLog: ServiceLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/service-log-update.component').then(m => m.ServiceLogUpdateComponent),
    resolve: {
      serviceLog: ServiceLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/service-log-update.component').then(m => m.ServiceLogUpdateComponent),
    resolve: {
      serviceLog: ServiceLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serviceLogRoute;
