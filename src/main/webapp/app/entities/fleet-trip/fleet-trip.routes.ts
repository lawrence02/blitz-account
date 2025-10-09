import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FleetTripResolve from './route/fleet-trip-routing-resolve.service';

const fleetTripRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fleet-trip.component').then(m => m.FleetTripComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fleet-trip-detail.component').then(m => m.FleetTripDetailComponent),
    resolve: {
      fleetTrip: FleetTripResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fleet-trip-update.component').then(m => m.FleetTripUpdateComponent),
    resolve: {
      fleetTrip: FleetTripResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fleet-trip-update.component').then(m => m.FleetTripUpdateComponent),
    resolve: {
      fleetTrip: FleetTripResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fleetTripRoute;
