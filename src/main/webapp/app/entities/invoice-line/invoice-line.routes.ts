import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import InvoiceLineResolve from './route/invoice-line-routing-resolve.service';

const invoiceLineRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/invoice-line.component').then(m => m.InvoiceLineComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/invoice-line-detail.component').then(m => m.InvoiceLineDetailComponent),
    resolve: {
      invoiceLine: InvoiceLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/invoice-line-update.component').then(m => m.InvoiceLineUpdateComponent),
    resolve: {
      invoiceLine: InvoiceLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/invoice-line-update.component').then(m => m.InvoiceLineUpdateComponent),
    resolve: {
      invoiceLine: InvoiceLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default invoiceLineRoute;
