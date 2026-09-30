package com.employee.dao;

import com.employee.model.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeDAOMockTest {

    @Test
    void testEmployeeMock() {

        Employee employee = mock(Employee.class);

        when(employee.getName()).thenReturn("Rohan");
        when(employee.getEmail()).thenReturn("rohan@test.com");
        when(employee.getDepartment()).thenReturn("IT");
        when(employee.getSalary()).thenReturn(30000.0);

        assertEquals("Rohan", employee.getName());
        assertEquals("rohan@test.com", employee.getEmail());
        assertEquals("IT", employee.getDepartment());
        assertEquals(30000.0, employee.getSalary());

        verify(employee).getName();
        verify(employee).getEmail();
        verify(employee).getDepartment();
        verify(employee).getSalary();
    }
}