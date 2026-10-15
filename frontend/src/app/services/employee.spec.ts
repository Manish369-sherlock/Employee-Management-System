import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting
} from '@angular/common/http/testing';

import { EmployeeService } from './employee';

describe('EmployeeService', () => {
  let service: EmployeeService;
  let httpMock: HttpTestingController;

  const apiUrl = 'http://localhost:8080/api/employees';

  const employee = {
    id: 1,
    name: 'Test Employee',
    email: 'test.employee@example.com',
    phone: '9876543210',
    department: 'IT',
    salary: 30000
  };

  const employeeInput = {
    name: 'Test Employee',
    email: 'test.employee@example.com',
    phone: '9876543210',
    department: 'IT',
    salary: 30000
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        EmployeeService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(EmployeeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create the service', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all employees', () => {
    service.getAllEmployees().subscribe(result => {
      expect(result).toEqual([employee]);
    });

    const request = httpMock.expectOne(apiUrl);
    expect(request.request.method).toBe('GET');

    request.flush([employee]);
  });

  it('should add an employee', () => {
    service.addEmployee(employeeInput).subscribe(result => {
      expect(result).toEqual(employee);
    });

    const request = httpMock.expectOne(apiUrl);

    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(employeeInput);

    request.flush(employee);
  });

  it('should update an employee', () => {
    service.updateEmployee(1, employeeInput).subscribe(result => {
      expect(result).toEqual(employee);
    });

    const request = httpMock.expectOne(`${apiUrl}/1`);

    expect(request.request.method).toBe('PUT');
    expect(request.request.body).toEqual(employeeInput);

    request.flush(employee);
  });

  it('should delete an employee', () => {
    service.deleteEmployee(1).subscribe();

    const request = httpMock.expectOne(`${apiUrl}/1`);

    expect(request.request.method).toBe('DELETE');

    request.flush(null, {
      status: 204,
      statusText: 'No Content'
    });
  });

  it('should handle a server error', () => {
    let receivedError = false;

    service.getAllEmployees().subscribe({
      next: () => {
        throw new Error('Expected the request to fail');
      },
      error: () => {
        receivedError = true;
      }
    });

    const request = httpMock.expectOne(apiUrl);

    request.flush('Server error', {
      status: 500,
      statusText: 'Internal Server Error'
    });

    expect(receivedError).toBe(true);
  });
});
