
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';

interface Employee {
  id: number;
  employeeCode: string;
  name: string;
  email: string;
  department: string;
  position: string;
  status: 'Active' | 'Inactive';
}

@Component({
  selector: 'app-employees',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './employees.html',
  styleUrl: './employees.css'
})
export class Employees {
  searchTerm = '';
  selectedDepartment = 'All';
  showForm = false;
  editingId: number | null = null;
  nextId = 4;

  departments = [
    'Engineering',
    'Human Resources',
    'Finance',
    'Marketing',
    'Sales',
    'Operations'
  ];

  employees: Employee[] = [
    {
      id: 1,
      employeeCode: 'EMP001',
      name: 'Rahul Sharma',
      email: 'rahul.sharma@example.com',
      department: 'Engineering',
      position: 'Software Engineer',
      status: 'Active'
    },
    {
      id: 2,
      employeeCode: 'EMP002',
      name: 'Priya Reddy',
      email: 'priya.reddy@example.com',
      department: 'Human Resources',
      position: 'HR Executive',
      status: 'Active'
    },
    {
      id: 3,
      employeeCode: 'EMP003',
      name: 'Arjun Kumar',
      email: 'arjun.kumar@example.com',
      department: 'Finance',
      position: 'Financial Analyst',
      status: 'Inactive'
    }
  ];

  formData = this.emptyEmployee();

  get filteredEmployees(): Employee[] {
    const term = this.searchTerm.trim().toLowerCase();

    return this.employees.filter((employee) => {
      const matchesSearch =
        !term ||
        employee.name.toLowerCase().includes(term) ||
        employee.email.toLowerCase().includes(term) ||
        employee.employeeCode.toLowerCase().includes(term);

      const matchesDepartment =
        this.selectedDepartment === 'All' ||
        employee.department === this.selectedDepartment;

      return matchesSearch && matchesDepartment;
    });
  }

  private emptyEmployee(): Omit<Employee, 'id'> {
    return {
      employeeCode: '',
      name: '',
      email: '',
      department: '',
      position: '',
      status: 'Active'
    };
  }

  openAddForm(): void {
    this.editingId = null;
    this.formData = this.emptyEmployee();
    this.showForm = true;
  }

  editEmployee(employee: Employee): void {
    this.editingId = employee.id;
    this.formData = {
      employeeCode: employee.employeeCode,
      name: employee.name,
      email: employee.email,
      department: employee.department,
      position: employee.position,
      status: employee.status
    };
    this.showForm = true;
  }

  saveEmployee(form: NgForm): void {
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    const employeeCode = this.formData.employeeCode.trim();
    const email = this.formData.email.trim().toLowerCase();

    const duplicate = this.employees.some((employee) =>
      employee.id !== this.editingId &&
      (employee.employeeCode.toLowerCase() === employeeCode.toLowerCase() ||
        employee.email.toLowerCase() === email)
    );

    if (duplicate) {
      window.alert('An employee with this ID or email already exists.');
      return;
    }

    const employeeData = {
      ...this.formData,
      employeeCode,
      name: this.formData.name.trim(),
      email,
      position: this.formData.position.trim()
    };

    if (this.editingId !== null) {
      this.employees = this.employees.map((employee) =>
        employee.id === this.editingId
          ? { ...employee, ...employeeData }
          : employee
      );
    } else {
      this.employees = [
        ...this.employees,
        { id: this.nextId++, ...employeeData }
      ];
    }

    this.closeForm();
  }

  deleteEmployee(id: number): void {
    const employee = this.employees.find((item) => item.id === id);

    if (!employee) {
      return;
    }

    const confirmed = window.confirm(
      `Are you sure you want to delete ${employee.name}?`
    );

    if (confirmed) {
      this.employees = this.employees.filter(
        (item) => item.id !== id
      );
    }
  }

  closeForm(): void {
    this.showForm = false;
    this.editingId = null;
    this.formData = this.emptyEmployee();
  }
}
