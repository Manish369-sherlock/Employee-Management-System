
package com.employee.backend.service;

import com.employee.backend.dao.EmployeeDAO;
import com.employee.backend.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeDAO employeeDAO;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(
                1,
                "Test Employee",
                "test.employee@example.com",
                "9876543210",
                "IT",
                30000.0
        );
    }

    @Test
    void shouldReturnAllEmployees() {
        when(employeeDAO.getAllEmployees())
                .thenReturn(List.of(employee));

        List<Employee> result =
                employeeService.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("Test Employee", result.get(0).getName());

        verify(employeeDAO).getAllEmployees();
    }

    @Test
    void shouldReturnEmployeeById() {
        when(employeeDAO.getEmployeeById(1))
                .thenReturn(Optional.of(employee));

        Optional<Employee> result =
                employeeService.getEmployeeById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenEmployeeDoesNotExist() {
        when(employeeDAO.getEmployeeById(99))
                .thenReturn(Optional.empty());

        Optional<Employee> result =
                employeeService.getEmployeeById(99);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAddEmployee() {
        when(employeeDAO.addEmployee(employee))
                .thenReturn(employee);

        Employee result =
                employeeService.addEmployee(employee);

        assertNotNull(result);
        assertEquals("Test Employee", result.getName());

        verify(employeeDAO).addEmployee(employee);
    }

    @Test
    void shouldUpdateEmployee() {
        when(employeeDAO.updateEmployee(1, employee))
                .thenReturn(true);

        boolean result =
                employeeService.updateEmployee(1, employee);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenUpdateFails() {
        when(employeeDAO.updateEmployee(99, employee))
                .thenReturn(false);

        boolean result =
                employeeService.updateEmployee(99, employee);

        assertFalse(result);
    }

    @Test
    void shouldDeleteEmployee() {
        when(employeeDAO.deleteEmployee(1))
                .thenReturn(true);

        boolean result =
                employeeService.deleteEmployee(1);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenDeleteFails() {
        when(employeeDAO.deleteEmployee(99))
                .thenReturn(false);

        boolean result =
                employeeService.deleteEmployee(99);

        assertFalse(result);
    }
}
