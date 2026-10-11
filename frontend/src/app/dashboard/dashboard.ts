
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Employee, EmployeeService } from '../services/employee';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  employees: Employee[] = [];
  loading = false;
  errorMessage = '';

  constructor(private readonly employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading = true;
    this.errorMessage = '';

    this.employeeService.getAllEmployees().subscribe({
      next: employees => {
        this.employees = employees;
        this.loading = false;
      },
      error: error => {
        this.loading = false;
        this.errorMessage =
          error.status === 0
            ? 'Cannot connect to the backend.'
            : 'Unable to load dashboard data. Please try again.';

        console.error('Unable to load dashboard:', error);
      }
    });
  }

  get totalEmployees(): number {
    return this.employees.length;
  }

  get totalSalary(): number {
    return this.employees.reduce(
      (total, employee) => total + Number(employee.salary || 0),
      0
    );
  }

  get departmentCount(): number {
    return this.departments.length;
  }

  get departments(): string[] {
    return [
      ...new Set(this.employees.map(employee => employee.department))
    ].sort((a, b) => a.localeCompare(b));
  }

  getDepartmentCount(department: string): number {
    return this.employees.filter(
      employee => employee.department === department
    ).length;
  }

  getDepartmentSalary(department: string): number {
    return this.employees
      .filter(employee => employee.department === department)
      .reduce(
        (total, employee) => total + Number(employee.salary || 0),
        0
      );
  }

  getDepartmentPercentage(department: string): number {
    if (this.totalEmployees === 0) {
      return 0;
    }

    return (
      this.getDepartmentCount(department) / this.totalEmployees
    ) * 100;
  }

  get recentEmployees(): Employee[] {
    return [...this.employees]
      .sort((a, b) => b.id - a.id)
      .slice(0, 5);
  }
}
