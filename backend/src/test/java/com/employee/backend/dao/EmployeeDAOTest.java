
package com.employee.backend.dao;

import com.employee.backend.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private EmployeeDAO employeeDAO;

    private Employee sampleEmployee() {
        return new Employee(
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
        when(jdbcTemplate.query(
                anyString(),
                any(RowMapper.class)
        )).thenReturn(List.of(sampleEmployee()));

        List<Employee> result = employeeDAO.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("Test Employee", result.get(0).getName());
    }

    @Test
    void shouldReturnEmployeeWhenFound() {
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(1)
        )).thenReturn(sampleEmployee());

        Optional<Employee> result =
                employeeDAO.getEmployeeById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenEmployeeIsMissing() {
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(99)
        )).thenThrow(
                new EmptyResultDataAccessException(1)
        );

        Optional<Employee> result =
                employeeDAO.getEmployeeById(99);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDeleteEmployeeWhenOneRowIsAffected() {
        when(jdbcTemplate.update(anyString(), eq(1)))
                .thenReturn(1);

        assertTrue(employeeDAO.deleteEmployee(1));
    }

    @Test
    void shouldReturnFalseWhenEmployeeCannotBeDeleted() {
        when(jdbcTemplate.update(anyString(), eq(99)))
                .thenReturn(0);

        assertFalse(employeeDAO.deleteEmployee(99));
    }
}
