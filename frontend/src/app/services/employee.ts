
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface Employee {
  id: number;
  name: string;
  email: string;
  phone: string;
  department: string;
  salary: number;
}

export type EmployeeInput = Omit<Employee, 'id'>;

export interface SalaryReport {
  totalEmployees: number;
  totalSalary: number;
  averageSalary: number;
  highestSalary: number;
  lowestSalary: number;
}

const USE_BACKEND_API = true;

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private readonly storageKey = 'employee-management-records';
  private readonly apiUrl = 'http://localhost:8080/api/employees';

  constructor(private readonly http: HttpClient) {}

  private loadEmployees(): Employee[] {
    try {
      const storedData = localStorage.getItem(this.storageKey);

      if (!storedData) {
        return [];
      }

      const parsedData: unknown = JSON.parse(storedData);

      if (!Array.isArray(parsedData)) {
        return [];
      }

      return parsedData as Employee[];
    } catch {
      return [];
    }
  }

  private saveEmployees(employees: Employee[]): void {
    localStorage.setItem(
      this.storageKey,
      JSON.stringify(employees)
    );
  }

  getAllEmployees(): Observable<Employee[]> {
    if (USE_BACKEND_API) {
      return this.http.get<Employee[]>(this.apiUrl);
    }

    return of(this.loadEmployees());
  }

  getEmployeeById(
    id: number
  ): Observable<Employee | undefined> {
    if (USE_BACKEND_API) {
      return this.http.get<Employee>(`${this.apiUrl}/${id}`).pipe(
        catchError(error => {
          if (error.status === 404) {
            return of(undefined);
          }

          return throwError(() => error);
        })
      );
    }

    const employee = this.loadEmployees().find(
      item => item.id === id
    );

    return of(employee);
  }

  addEmployee(employee: EmployeeInput): Observable<Employee> {
    if (USE_BACKEND_API) {
      return this.http.post<Employee>(this.apiUrl, employee);
    }

    const employees = this.loadEmployees();

    const newEmployee: Employee = {
      ...employee,
      id: employees.length
        ? Math.max(...employees.map(item => item.id)) + 1
        : 1
    };

    employees.push(newEmployee);
    this.saveEmployees(employees);

    return of(newEmployee);
  }

  updateEmployee(
    id: number,
    employee: EmployeeInput
  ): Observable<void> {
    if (USE_BACKEND_API) {
      return this.http.put<void>(
        `${this.apiUrl}/${id}`,
        employee
      );
    }

    const employees = this.loadEmployees();
    const index = employees.findIndex(
      item => item.id === id
    );

    if (index === -1) {
      return throwError(
        () => new Error('Employee not found.')
      );
    }

    employees[index] = {
      id,
      ...employee
    };

    this.saveEmployees(employees);

    return of(undefined);
  }

  deleteEmployee(id: number): Observable<void> {
    if (USE_BACKEND_API) {
      return this.http.delete<void>(
        `${this.apiUrl}/${id}`
      );
    }

    const employees = this.loadEmployees();
    const remainingEmployees = employees.filter(
      item => item.id !== id
    );

    if (remainingEmployees.length === employees.length) {
      return throwError(
        () => new Error('Employee not found.')
      );
    }

    this.saveEmployees(remainingEmployees);

    return of(undefined);
  }

  getEmployeesByDepartment(department: string): Observable<Employee[]> {
    if (USE_BACKEND_API) {
      return this.http.get<Employee[]>(
        `${this.apiUrl}/department/${encodeURIComponent(department)}`
      );
    }

    const employees = this.loadEmployees().filter(
      item => item.department.toLowerCase() === department.toLowerCase()
    );
    return of(employees);
  }

  getSalaryReport(): Observable<SalaryReport> {
    if (USE_BACKEND_API) {
      return this.http.get<SalaryReport>(`${this.apiUrl}/salary-report`);
    }

    const employees = this.loadEmployees();
    const count = employees.length;
    if (count === 0) {
      return of({
        totalEmployees: 0,
        totalSalary: 0,
        averageSalary: 0,
        highestSalary: 0,
        lowestSalary: 0
      });
    }

    const salaries = employees.map(e => Number(e.salary || 0));
    const totalSalary = salaries.reduce((a, b) => a + b, 0);
    return of({
      totalEmployees: count,
      totalSalary,
      averageSalary: Math.round((totalSalary / count) * 100) / 100,
      highestSalary: Math.max(...salaries),
      lowestSalary: Math.min(...salaries)
    });
  }
}
