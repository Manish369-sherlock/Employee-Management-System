
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    title: 'Dashboard | EmployeeHub',
    loadComponent: () =>
      import('./dashboard/dashboard').then(m => m.Dashboard)
  },
  {
    path: 'employees',
    title: 'Employees | EmployeeHub',
    loadComponent: () =>
      import('./employees/employees').then(m => m.Employees)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
