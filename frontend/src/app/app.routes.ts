
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    title: 'Dashboard | EmployEase',
    loadComponent: () =>
      import('./dashboard/dashboard').then((m) => m.Dashboard)
  },
  {
    path: 'employees',
    title: 'Employees | EmployEase',
    loadComponent: () =>
      import('./employees/employees').then((m) => m.Employees)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
