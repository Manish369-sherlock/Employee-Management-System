
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';
import { vi, afterEach, describe, expect, it, beforeEach } from 'vitest';

import { Employees } from './employees';
import { Employee, EmployeeService } from '../services/employee';

describe('Employees', () => {
  let component: Employees;
  let fixture: ComponentFixture<Employees>;
  let employeeService: {
    getAllEmployees: ReturnType<typeof vi.fn>;
    getEmployeeById: ReturnType<typeof vi.fn>;
    addEmployee: ReturnType<typeof vi.fn>;
    updateEmployee: ReturnType<typeof vi.fn>;
    deleteEmployee: ReturnType<typeof vi.fn>;
    getEmployeesByDepartment: ReturnType<typeof vi.fn>;
    getSalaryReport: ReturnType<typeof vi.fn>;
  };

  const employees: Employee[] = [
    {
      id: 1,
      name: 'Rahul Sharma',
      email: 'rahul@example.com',
      phone: '9876543210',
      department: 'Engineering',
      salary: 50000
    },
    {
      id: 2,
      name: 'Priya Reddy',
      email: 'priya@example.com',
      phone: '9876543211',
      department: 'Finance',
      salary: 60000
    },
    {
      id: 3,
      name: 'Arjun Kumar',
      email: 'arjun@example.com',
      phone: '9876543212',
      department: 'Engineering',
      salary: 45000
    }
  ];

  beforeEach(async () => {
    employeeService = {
      getAllEmployees: vi.fn().mockReturnValue(of(employees)),
      getEmployeeById: vi.fn().mockReturnValue(of(employees[0])),
      addEmployee: vi.fn().mockReturnValue(of({
        ...employees[0]
      })),
      updateEmployee: vi.fn().mockReturnValue(of(undefined)),
      deleteEmployee: vi.fn().mockReturnValue(of(undefined)),
      getEmployeesByDepartment: vi.fn().mockReturnValue(of([])),
      getSalaryReport: vi.fn().mockReturnValue(of({
        totalEmployees: 3,
        totalSalary: 155000,
        averageSalary: 51666.67,
        highestSalary: 60000,
        lowestSalary: 45000
      }))
    };

    await TestBed.configureTestingModule({
      imports: [Employees],
      providers: [
        {
          provide: EmployeeService,
          useValue: employeeService
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Employees);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load employees on initialization', () => {
    expect(employeeService.getAllEmployees).toHaveBeenCalled();
    expect(component.employees).toEqual(employees);
    expect(component.loading).toBe(false);
    expect(component.errorMessage).toBe('');
  });

  it('should handle an empty employee list', () => {
    employeeService.getAllEmployees.mockReturnValueOnce(of([]));

    component.loadEmployees();

    expect(component.employees).toEqual([]);
    expect(component.loading).toBe(false);
  });

  it('should display an error when employee loading fails', () => {
    employeeService.getAllEmployees.mockReturnValueOnce(
      throwError(() => new HttpErrorResponse({ status: 500 }))
    );

    component.loadEmployees();

    expect(component.loading).toBe(false);
    expect(component.errorMessage).toBe(
      'Unable to load employees. Check the backend and database.'
    );
  });

  it('should show a backend connection error when loading fails with status zero', () => {
    employeeService.getAllEmployees.mockReturnValueOnce(
      throwError(() => new HttpErrorResponse({ status: 0 }))
    );

    component.loadEmployees();

    expect(component.errorMessage).toBe(
      'Cannot connect to the backend. Check that Spring Boot is running.'
    );
  });

  it('should reject an invalid backend response format', () => {
    employeeService.getAllEmployees.mockReturnValueOnce(of(null as never));

    component.loadEmployees();

    expect(component.employees).toEqual([]);
    expect(component.loading).toBe(false);
    expect(component.errorMessage).toContain(
      'backend response format is invalid'
    );
  });

  it('should search employees by name', () => {
    component.searchTerm = 'rahul';

    expect(component.filteredEmployees.map(e => e.id)).toEqual([1]);
  });

  it('should search employees by email', () => {
    component.searchTerm = 'priya@example.com';

    expect(component.filteredEmployees.map(e => e.id)).toEqual([2]);
  });

  it('should search employees by ID', () => {
    component.searchTerm = '3';

    expect(component.filteredEmployees.map(e => e.id)).toEqual([3]);
  });

  it('should ignore case and surrounding spaces while searching', () => {
    component.searchTerm = '  RAHUL  ';

    expect(component.filteredEmployees.map(e => e.id)).toEqual([1]);
  });

  it('should filter employees by department', () => {
    component.selectedDepartment = 'Engineering';

    expect(component.filteredEmployees.map(e => e.id)).toEqual([1, 3]);
  });

  it('should combine search and department filters', () => {
    component.searchTerm = 'arjun';
    component.selectedDepartment = 'Engineering';

    expect(component.filteredEmployees.map(e => e.id)).toEqual([3]);
  });

  it('should return all employees when department is All', () => {
    component.selectedDepartment = 'All';

    expect(component.filteredEmployees).toEqual(employees);
  });

  it('should include existing departments in the department list', () => {
    expect(component.departments).toContain('Engineering');
    expect(component.departments).toContain('Finance');
    expect(component.departments).toContain('Marketing');
  });

  it('should open the add form with empty fields', () => {
    component.openAddForm();

    expect(component.showForm).toBe(true);
    expect(component.editingId).toBeNull();
    expect(component.formData.name).toBe('');
    expect(component.formData.salary).toBe(0);
  });

  it('should populate the form when editing an employee', () => {
    component.editEmployee(employees[1]);

    expect(component.showForm).toBe(true);
    expect(component.editingId).toBe(2);
    expect(component.formData.name).toBe('Priya Reddy');
    expect(component.formData.email).toBe('priya@example.com');
    expect(component.formData.department).toBe('Finance');
    expect(component.formData.salary).toBe(60000);
  });

  it('should close the form and reset its fields', () => {
    component.editEmployee(employees[0]);
    component.closeForm();

    expect(component.showForm).toBe(false);
    expect(component.editingId).toBeNull();
    expect(component.formData.name).toBe('');
  });

  it('should reject an invalid form without calling the service', () => {
    const form = {
      invalid: true,
      control: {
        markAllAsTouched: vi.fn()
      }
    } as never;

    component.saveEmployee(form);

    expect(employeeService.addEmployee).not.toHaveBeenCalled();
    expect(employeeService.updateEmployee).not.toHaveBeenCalled();
  });

  it('should reject a duplicate email address', () => {
    component.openAddForm();
    component.formData = {
      name: 'Another Employee',
      email: 'RAHUL@example.com',
      phone: '9876543213',
      department: 'Engineering',
      salary: 30000
    };

    const form = {
      invalid: false,
      control: {
        markAllAsTouched: vi.fn()
      }
    } as never;

    component.saveEmployee(form);

    expect(component.errorMessage).toBe(
      'An employee with this email already exists.'
    );
    expect(employeeService.addEmployee).not.toHaveBeenCalled();
  });

  it('should reject a non-positive salary', () => {
    component.openAddForm();
    component.formData = {
      name: 'New Employee',
      email: 'new@example.com',
      phone: '9876543213',
      department: 'Engineering',
      salary: 0
    };

    const form = {
      invalid: false,
      control: {
        markAllAsTouched: vi.fn()
      }
    } as never;

    component.saveEmployee(form);

    expect(component.errorMessage).toContain(
      'Salary must be greater than zero'
    );
    expect(employeeService.addEmployee).not.toHaveBeenCalled();
  });

  it('should add an employee and reload the list', () => {
    component.openAddForm();
    component.formData = {
      name: 'New Employee',
      email: 'new@example.com',
      phone: '9876543213',
      department: 'Engineering',
      salary: 35000
    };

    const form = {
      invalid: false,
      control: {
        markAllAsTouched: vi.fn()
      }
    } as never;

    component.saveEmployee(form);

    expect(employeeService.addEmployee).toHaveBeenCalledWith({
      name: 'New Employee',
      email: 'new@example.com',
      phone: '9876543213',
      department: 'Engineering',
      salary: 35000
    });

    expect(component.showForm).toBe(false);
    expect(component.successMessage).toBe(
      'Employee added successfully.'
    );
    expect(employeeService.getAllEmployees).toHaveBeenCalledTimes(2);
  });

  it('should show a conflict error when an email is already registered', () => {
    employeeService.addEmployee.mockReturnValueOnce(
      throwError(() => new HttpErrorResponse({ status: 409 }))
    );

    component.openAddForm();
    component.formData = {
      name: 'New Employee',
      email: 'new@example.com',
      phone: '9876543213',
      department: 'Engineering',
      salary: 35000
    };

    const form = {
      invalid: false,
      control: {
        markAllAsTouched: vi.fn()
      }
    } as never;

    component.saveEmployee(form);

    expect(component.loading).toBe(false);
    expect(component.errorMessage).toBe(
      'This email is already registered.'
    );
  });

  it('should update an existing employee', () => {
    component.editEmployee(employees[0]);
    component.formData = {
      name: 'Rahul Updated',
      email: 'rahul@example.com',
      phone: '9876543210',
      department: 'Engineering',
      salary: 55000
    };

    const form = {
      invalid: false,
      control: {
        markAllAsTouched: vi.fn()
      }
    } as never;

    component.saveEmployee(form);

    expect(employeeService.updateEmployee).toHaveBeenCalledWith(
  1,
  {
    name: 'Rahul Updated',
    email: 'rahul@example.com',
    phone: '9876543210',
    department: 'Engineering',
    salary: 55000
  }
);
    expect(component.successMessage).toBe(
      'Employee updated successfully.'
    );
  });

  it('should cancel deletion when the user does not confirm', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(false);

    component.deleteEmployee(1);

    expect(employeeService.deleteEmployee).not.toHaveBeenCalled();
  });

  it('should delete an employee after confirmation', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);

    component.deleteEmployee(1);

    expect(employeeService.deleteEmployee).toHaveBeenCalledWith(1);
    expect(component.successMessage).toBe(
      'Employee deleted successfully.'
    );
    expect(employeeService.getAllEmployees).toHaveBeenCalledTimes(2);
  });

  it('should not delete an employee that is not in the list', () => {
    component.deleteEmployee(999);

    expect(employeeService.deleteEmployee).not.toHaveBeenCalled();
  });

  it('should display an error when deletion fails', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    employeeService.deleteEmployee.mockReturnValueOnce(
      throwError(() => new HttpErrorResponse({ status: 500 }))
    );

    component.deleteEmployee(1);

    expect(component.loading).toBe(false);
    expect(component.errorMessage).toBe(
      'Unable to delete employee. Please try again.'
    );
  });
});
