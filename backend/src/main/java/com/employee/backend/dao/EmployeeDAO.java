
package com.employee.backend.dao;

import com.employee.backend.model.Employee;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDAO {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Employee mapEmployee(java.sql.ResultSet rs, int rowNum)
            throws java.sql.SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("department"),
                rs.getDouble("salary")
        );
    }

    public List<Employee> getAllEmployees() {
        String sql = """
                SELECT id, name, email, phone, department, salary
                FROM employees
                ORDER BY id
                """;

        return jdbcTemplate.query(sql, this::mapEmployee);
    }

    public Optional<Employee> getEmployeeById(int id) {
        String sql = """
                SELECT id, name, email, phone, department, salary
                FROM employees
                WHERE id = ?
                """;

        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, this::mapEmployee, id)
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public Employee addEmployee(Employee employee) {
        String sql = """
                INSERT INTO employees (name, email, phone, department, salary)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setString(3, employee.getPhone());
            statement.setString(4, employee.getDepartment());
            statement.setDouble(5, employee.getSalary());
            return statement;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            employee.setId(keyHolder.getKey().intValue());
        }

        return employee;
    }

    public boolean updateEmployee(int id, Employee employee) {
        String sql = """
                UPDATE employees
                SET name = ?, email = ?, phone = ?, department = ?, salary = ?
                WHERE id = ?
                """;

        int rows = jdbcTemplate.update(
                sql,
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getDepartment(),
                employee.getSalary(),
                id
        );

        return rows > 0;
    }

    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}
