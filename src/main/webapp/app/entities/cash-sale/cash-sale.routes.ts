import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CashSaleResolve from './route/cash-sale-routing-resolve.service';

const cashSaleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cash-sale.component').then(m => m.CashSaleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cash-sale-detail.component').then(m => m.CashSaleDetailComponent),
    resolve: {
      cashSale: CashSaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cash-sale-update.component').then(m => m.CashSaleUpdateComponent),
    resolve: {
      cashSale: CashSaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cash-sale-update.component').then(m => m.CashSaleUpdateComponent),
    resolve: {
      cashSale: CashSaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cashSaleRoute;
