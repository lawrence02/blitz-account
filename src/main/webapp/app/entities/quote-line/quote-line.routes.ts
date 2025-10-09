import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import QuoteLineResolve from './route/quote-line-routing-resolve.service';

const quoteLineRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/quote-line.component').then(m => m.QuoteLineComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/quote-line-detail.component').then(m => m.QuoteLineDetailComponent),
    resolve: {
      quoteLine: QuoteLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/quote-line-update.component').then(m => m.QuoteLineUpdateComponent),
    resolve: {
      quoteLine: QuoteLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/quote-line-update.component').then(m => m.QuoteLineUpdateComponent),
    resolve: {
      quoteLine: QuoteLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default quoteLineRoute;
