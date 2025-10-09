import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'blitzAccountApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'currency',
    data: { pageTitle: 'blitzAccountApp.currency.home.title' },
    loadChildren: () => import('./currency/currency.routes'),
  },
  {
    path: 'exchange-rate',
    data: { pageTitle: 'blitzAccountApp.exchangeRate.home.title' },
    loadChildren: () => import('./exchange-rate/exchange-rate.routes'),
  },
  {
    path: 'vat-rate',
    data: { pageTitle: 'blitzAccountApp.vATRate.home.title' },
    loadChildren: () => import('./vat-rate/vat-rate.routes'),
  },
  {
    path: 'chart-of-account',
    data: { pageTitle: 'blitzAccountApp.chartOfAccount.home.title' },
    loadChildren: () => import('./chart-of-account/chart-of-account.routes'),
  },
  {
    path: 'bank-account',
    data: { pageTitle: 'blitzAccountApp.bankAccount.home.title' },
    loadChildren: () => import('./bank-account/bank-account.routes'),
  },
  {
    path: 'bank-transaction',
    data: { pageTitle: 'blitzAccountApp.bankTransaction.home.title' },
    loadChildren: () => import('./bank-transaction/bank-transaction.routes'),
  },
  {
    path: 'transaction',
    data: { pageTitle: 'blitzAccountApp.transaction.home.title' },
    loadChildren: () => import('./transaction/transaction.routes'),
  },
  {
    path: 'employee',
    data: { pageTitle: 'blitzAccountApp.employee.home.title' },
    loadChildren: () => import('./employee/employee.routes'),
  },
  {
    path: 'supplier',
    data: { pageTitle: 'blitzAccountApp.supplier.home.title' },
    loadChildren: () => import('./supplier/supplier.routes'),
  },
  {
    path: 'product-category',
    data: { pageTitle: 'blitzAccountApp.productCategory.home.title' },
    loadChildren: () => import('./product-category/product-category.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'blitzAccountApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'payment',
    data: { pageTitle: 'blitzAccountApp.payment.home.title' },
    loadChildren: () => import('./payment/payment.routes'),
  },
  {
    path: 'quote',
    data: { pageTitle: 'blitzAccountApp.quote.home.title' },
    loadChildren: () => import('./quote/quote.routes'),
  },
  {
    path: 'quote-line',
    data: { pageTitle: 'blitzAccountApp.quoteLine.home.title' },
    loadChildren: () => import('./quote-line/quote-line.routes'),
  },
  {
    path: 'invoice',
    data: { pageTitle: 'blitzAccountApp.invoice.home.title' },
    loadChildren: () => import('./invoice/invoice.routes'),
  },
  {
    path: 'invoice-line',
    data: { pageTitle: 'blitzAccountApp.invoiceLine.home.title' },
    loadChildren: () => import('./invoice-line/invoice-line.routes'),
  },
  {
    path: 'cash-sale',
    data: { pageTitle: 'blitzAccountApp.cashSale.home.title' },
    loadChildren: () => import('./cash-sale/cash-sale.routes'),
  },
  {
    path: 'vehicle',
    data: { pageTitle: 'blitzAccountApp.vehicle.home.title' },
    loadChildren: () => import('./vehicle/vehicle.routes'),
  },
  {
    path: 'fleet-trip',
    data: { pageTitle: 'blitzAccountApp.fleetTrip.home.title' },
    loadChildren: () => import('./fleet-trip/fleet-trip.routes'),
  },
  {
    path: 'fuel-log',
    data: { pageTitle: 'blitzAccountApp.fuelLog.home.title' },
    loadChildren: () => import('./fuel-log/fuel-log.routes'),
  },
  {
    path: 'service-log',
    data: { pageTitle: 'blitzAccountApp.serviceLog.home.title' },
    loadChildren: () => import('./service-log/service-log.routes'),
  },
  {
    path: 'incident-log',
    data: { pageTitle: 'blitzAccountApp.incidentLog.home.title' },
    loadChildren: () => import('./incident-log/incident-log.routes'),
  },
  {
    path: 'budget',
    data: { pageTitle: 'blitzAccountApp.budget.home.title' },
    loadChildren: () => import('./budget/budget.routes'),
  },
  {
    path: 'recurring-transaction',
    data: { pageTitle: 'blitzAccountApp.recurringTransaction.home.title' },
    loadChildren: () => import('./recurring-transaction/recurring-transaction.routes'),
  },
  {
    path: 'journal',
    data: { pageTitle: 'blitzAccountApp.journal.home.title' },
    loadChildren: () => import('./journal/journal.routes'),
  },
  {
    path: 'journal-line',
    data: { pageTitle: 'blitzAccountApp.journalLine.home.title' },
    loadChildren: () => import('./journal-line/journal-line.routes'),
  },
  {
    path: 'fleet-trip-location',
    data: { pageTitle: 'blitzAccountApp.fleetTripLocation.home.title' },
    loadChildren: () => import('./fleet-trip-location/fleet-trip-location.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
