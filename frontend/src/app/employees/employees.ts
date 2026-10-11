
import { Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import {
  Employee,
  EmployeeInput,
  EmployeeService
} from '../services/employee';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-employees',
  standalone: true,
  imports: [FormsModule, DecimalPipe],
  templateUrl: './employees.html',
  styleUrl: './employees.css'
})
export class Employees implements OnInit {
  searchTerm = '';
  selectedDepartment = 'All';
  showForm = false;
  editingId: number | null = null;
  loading = false;
  errorMessage = '';
  successMessage = '';

  departments = [
    'Engineering',
    'Human Resources',
    'Finance',
    'Marketing',
    'Sales',
    'Operations'
  ];

  employees: Employee[] = [];
  formData: EmployeeInput = this.emptyEmployee();

  constructor(private readonly employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  get filteredEmployees(): Employee[] {
    const term = this.searchTerm.trim().toLowerCase();

    return this.employees.filter((employee) => {
      const matchesSearch =
        !term ||
        employee.name.toLowerCase().includes(term) ||
        employee.email.toLowerCase().includes(term) ||
        String(employee.id).includes(term);

      const matchesDepartment =
        this.selectedDepartment === 'All' ||
        employee.department === this.selectedDepartment;

      return matchesSearch && matchesDepartment;
    });
  }

  private emptyEmployee(): EmployeeInput {
    return {
      name: '',
      email: '',
      phone: '',
      department: '',
      salary: 0
    };
  }

  loadEmployees(): void {
    this.loading = true;
    this.errorMessage = '';

    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees = data;
        this.loading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false;
        this.errorMessage =
          error.status === 0
            ? 'Cannot connect to the backend. Check that Spring Boot is running.'
            : 'Unable to load employees. Check the backend and database.';
        console.error('Failed to load employees:', error);
      }
    });
  }

  openAddForm(): void {
    this.editingId = null;
    this.formData = this.emptyEmployee();
    this.errorMessage = '';
    this.successMessage = '';
    this.showForm = true;
  }

  editEmployee(employee: Employee): void {
    this.editingId = employee.id;
    this.formData = {
      name: employee.name,
      email: employee.email,
      phone: employee.phone,
      department: employee.department,
      salary: employee.salary
    };

    this.errorMessage = '';
    this.successMessage = '';
    this.showForm = true;
  }

  saveEmployee(form: NgForm): void {
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    const payload: EmployeeInput = {
      name: this.formData.name.trim(),
      email: this.formData.email.trim(),
      phone: this.formData.phone.trim(),
      department: this.formData.department,
      salary: Number(this.formData.salary)
    };

    if (
      !payload.name ||
      !payload.email ||
      !payload.phone ||
      !payload.department ||
      !Number.isFinite(payload.salary) ||
      payload.salary < 0
    ) {
      this.errorMessage = 'Please enter valid employee details.';
      return;
    }

    const isEditing = this.editingId !== null;
    const employeeId = this.editingId;

    const duplicate = this.employees.some(
      (employee) =>
        employee.id !== employeeId &&
        employee.email.toLowerCase() === payload.email.toLowerCase()
    );

    if (duplicate) {
      this.errorMessage = 'An employee with this email already exists.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (isEditing && employeeId !== null) {
      this.employeeService.updateEmployee(employeeId, payload).subscribe({
        next: () => {
          this.loading = false;
          this.closeForm();
          this.successMessage = 'Employee updated successfully.';
          this.loadEmployees();
        },
        error: (error: HttpErrorResponse) => {
          this.handleSaveError(error);
        }
      });
    } else {
      this.employeeService.addEmployee(payload).subscribe({
        next: () => {
          this.loading = false;
          this.closeForm();
          this.successMessage = 'Employee added successfully.';
          this.loadEmployees();
        },
        error: (error: HttpErrorResponse) => {
          this.handleSaveError(error);
        }
      });
    }
  }

  private handleSaveError(error: HttpErrorResponse): void {
    this.loading = false;
    this.errorMessage =
      error.status === 0
        ? 'Cannot connect to the backend. Check that Spring Boot is running.'
        : error.status === 409
          ? 'This email is already registered.'
          : 'Unable to save employee. Check the details and try again.';

    console.error('Failed to save employee:', error);
  }

  deleteEmployee(id: number): void {
    const employee = this.employees.find((item) => item.id === id);
    if (!employee) return;

    const confirmed = window.confirm(
      `Are you sure you want to delete ${employee.name}?`
    );
    if (!confirmed) return;

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.employeeService.deleteEmployee(id).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Employee deleted successfully.';
        this.loadEmployees();
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false;
        this.errorMessage =
          error.status === 0
            ? 'Cannot connect to the backend. Check that Spring Boot is running.'
            : 'Unable to delete employee. Please try again.';
        console.error('Failed to delete employee:', error);
      }
    });
  }

  closeForm(): void {
    this.showForm = false;
    this.editingId = null;
    this.formData = this.emptyEmployee();
  }
}
