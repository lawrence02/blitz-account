import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import QuoteResolve from './route/quote-routing-resolve.service';

const quoteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/quote.component').then(m => m.QuoteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/quote-detail.component').then(m => m.QuoteDetailComponent),
    resolve: {
      quote: QuoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/quote-update.component').then(m => m.QuoteUpdateComponent),
    resolve: {
      quote: QuoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/quote-update.component').then(m => m.QuoteUpdateComponent),
    resolve: {
      quote: QuoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default quoteRoute;
