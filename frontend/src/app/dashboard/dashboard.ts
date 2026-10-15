import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import {
  Employee,
  EmployeeService
} from '../services/employee';

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

  constructor(
    private readonly employeeService: EmployeeService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading = true;
    this.errorMessage = '';
    this.cdr.markForCheck();

    console.log('Dashboard loading started');

    this.employeeService.getAllEmployees().subscribe({
      next: (employees: Employee[]) => {
        console.log('Dashboard API response:', employees);

        this.employees = Array.isArray(employees)
          ? employees
          : [];

        this.loading = false;
        this.cdr.markForCheck();

        console.log('Dashboard loading:', this.loading);
        console.log(
          'Total employees:',
          this.employees.length
        );
      },

      error: (error: HttpErrorResponse) => {
        console.error(
          'Unable to load dashboard:',
          error
        );

        this.loading = false;
        this.errorMessage =
          error.status === 0
            ? 'Cannot connect to the backend. Check that Spring Boot is running.'
            : 'Unable to load dashboard data. Please try again.';
        this.cdr.markForCheck();
      },

      complete: () => {
        console.log('Dashboard request completed');
      }
    });
  }

  get totalEmployees(): number {
    return this.employees.length;
  }

  get totalSalary(): number {
    return this.employees.reduce(
      (total, employee) =>
        total + Number(employee.salary || 0),
      0
    );
  }

  get averageSalary(): number {
    if (this.totalEmployees === 0) {
      return 0;
    }
    return this.totalSalary / this.totalEmployees;
  }

  get highestSalary(): number {
    if (this.totalEmployees === 0) {
      return 0;
    }
    return Math.max(...this.employees.map(e => Number(e.salary || 0)));
  }

  get lowestSalary(): number {
    if (this.totalEmployees === 0) {
      return 0;
    }
    return Math.min(...this.employees.map(e => Number(e.salary || 0)));
  }

  get departmentCount(): number {
    return this.departments.length;
  }

  get departments(): string[] {
    return [
      ...new Set(
        this.employees.map(
          employee => employee.department
        )
      )
    ].sort((a, b) => a.localeCompare(b));
  }

  getDepartmentCount(department: string): number {
    return this.employees.filter(
      employee => employee.department === department
    ).length;
  }

  getDepartmentSalary(department: string): number {
    return this.employees
      .filter(
        employee => employee.department === department
      )
      .reduce(
        (total, employee) =>
          total + Number(employee.salary || 0),
        0
      );
  }

  getDepartmentPercentage(department: string): number {
    if (this.totalEmployees === 0) {
      return 0;
    }

    return (
      this.getDepartmentCount(department) /
      this.totalEmployees
    ) * 100;
  }

  /** Departments sorted by total salary descending — used by the salary report table */
  get salaryReportRows(): { department: string; count: number; totalSalary: number }[] {
    return this.departments
      .map(department => ({
        department,
        count: this.getDepartmentCount(department),
        totalSalary: this.getDepartmentSalary(department)
      }))
      .sort((a, b) => b.totalSalary - a.totalSalary);
  }

  get recentEmployees(): Employee[] {
    return [...this.employees]
      .sort((a, b) => b.id - a.id)
      .slice(0, 5);
  }
}