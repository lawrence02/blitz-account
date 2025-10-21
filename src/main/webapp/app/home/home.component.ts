import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { VehicleService } from '../entities/vehicle/service/vehicle.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account = signal<Account | null>(null);

  // Dashboard data
  vehicleStats = signal({
    available: 0,
    inTrip: 0,
    maintenance: 0,
    idle: 0,
    total: 0,
  });

  incidentStats = signal({
    accidents: 0,
    breakdowns: 0,
    dents: 0,
    total: 0,
    thisMonth: 0,
  });

  invoiceStats = signal({
    outstanding: 0,
    overdue: 0,
    totalAmount: 0,
    overdueAmount: 0,
  });

  tripStats = signal({
    activeTrips: 0,
    completedToday: 0,
    scheduledTomorrow: 0,
  });

  financialStats = signal({
    monthlyRevenue: 0,
    monthlyExpenses: 0,
    netProfit: 0,
  });

  recentAlerts = signal<any[]>([]);

  private readonly destroy$ = new Subject<void>();

  private readonly accountService = inject(AccountService);
  private readonly vehicleStatsService = inject(VehicleService);
  private readonly router = inject(Router);

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => this.account.set(account));

    this.loadDashboardData();
  }

  loadDashboardData(): void {
    // TODO: Replace with actual API calls
    this.loadVehicleStats();
    this.loadIncidentStats();
    this.loadInvoiceStats();
    this.loadTripStats();
    this.loadFinancialStats();
    this.loadRecentAlerts();
  }

  loadVehicleStats(): void {
    // Example: this.vehicleService.getStats().subscribe(data => {
    //   this.vehicleStats.set(data);
    // });

    // Mock data for now
    /* this.vehicleStats.set({
      available: 12,
      inTrip: 8,
      maintenance: 3,
      idle: 2,
      total: 25,
    }); */

    this.vehicleStatsService.getVehicleStats().subscribe({
      next: res => {
        if (res.body) {
          this.vehicleStats.set(res.body);
        }
      },
      error: err => {
        console.error('Error fetching vehicle stats', err);
      },
    });
  }

  loadIncidentStats(): void {
    this.incidentStats.set({
      accidents: 2,
      breakdowns: 5,
      dents: 3,
      total: 10,
      thisMonth: 10,
    });
  }

  loadInvoiceStats(): void {
    this.invoiceStats.set({
      outstanding: 15,
      overdue: 4,
      totalAmount: 45000,
      overdueAmount: 8500,
    });
  }

  loadTripStats(): void {
    this.tripStats.set({
      activeTrips: 8,
      completedToday: 12,
      scheduledTomorrow: 6,
    });
  }

  loadFinancialStats(): void {
    this.financialStats.set({
      monthlyRevenue: 125000,
      monthlyExpenses: 78000,
      netProfit: 47000,
    });
  }

  loadRecentAlerts(): void {
    this.recentAlerts.set([
      { type: 'warning', message: 'Vehicle ZW-123 service due in 500km', icon: 'wrench' },
      { type: 'danger', message: '4 invoices overdue (total $8,500)', icon: 'exclamation-triangle' },
      { type: 'info', message: 'Driver license expires in 15 days - John Doe', icon: 'id-card' },
    ]);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
