import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import IncidentLogResolve from './route/incident-log-routing-resolve.service';

const incidentLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/incident-log.component').then(m => m.IncidentLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/incident-log-detail.component').then(m => m.IncidentLogDetailComponent),
    resolve: {
      incidentLog: IncidentLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/incident-log-update.component').then(m => m.IncidentLogUpdateComponent),
    resolve: {
      incidentLog: IncidentLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/incident-log-update.component').then(m => m.IncidentLogUpdateComponent),
    resolve: {
      incidentLog: IncidentLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default incidentLogRoute;
