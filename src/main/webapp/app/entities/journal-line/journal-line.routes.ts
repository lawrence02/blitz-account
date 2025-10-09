import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import JournalLineResolve from './route/journal-line-routing-resolve.service';

const journalLineRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/journal-line.component').then(m => m.JournalLineComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/journal-line-detail.component').then(m => m.JournalLineDetailComponent),
    resolve: {
      journalLine: JournalLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/journal-line-update.component').then(m => m.JournalLineUpdateComponent),
    resolve: {
      journalLine: JournalLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/journal-line-update.component').then(m => m.JournalLineUpdateComponent),
    resolve: {
      journalLine: JournalLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default journalLineRoute;
