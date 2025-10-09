import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FleetTripLocationResolve from './route/fleet-trip-location-routing-resolve.service';

const fleetTripLocationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fleet-trip-location.component').then(m => m.FleetTripLocationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fleet-trip-location-detail.component').then(m => m.FleetTripLocationDetailComponent),
    resolve: {
      fleetTripLocation: FleetTripLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fleet-trip-location-update.component').then(m => m.FleetTripLocationUpdateComponent),
    resolve: {
      fleetTripLocation: FleetTripLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fleet-trip-location-update.component').then(m => m.FleetTripLocationUpdateComponent),
    resolve: {
      fleetTripLocation: FleetTripLocationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fleetTripLocationRoute;
