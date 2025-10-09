import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import JournalResolve from './route/journal-routing-resolve.service';

const journalRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/journal.component').then(m => m.JournalComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/journal-detail.component').then(m => m.JournalDetailComponent),
    resolve: {
      journal: JournalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/journal-update.component').then(m => m.JournalUpdateComponent),
    resolve: {
      journal: JournalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/journal-update.component').then(m => m.JournalUpdateComponent),
    resolve: {
      journal: JournalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default journalRoute;
