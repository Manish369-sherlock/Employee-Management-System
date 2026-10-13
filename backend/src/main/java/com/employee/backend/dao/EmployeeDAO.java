
package com.employee.backend.dao;

import com.employee.backend.model.Employee;
import com.employee.backend.model.SalaryReport;
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
        if (getEmployeeById(id).isEmpty()) {
            return false;
        }

        String sql = """
                UPDATE employees
                SET name = ?, email = ?, phone = ?, department = ?, salary = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(
                sql,
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getDepartment(),
                employee.getSalary(),
                id
        );

        return true;
    }

    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        String sql = """
                SELECT id, name, email, phone, department, salary
                FROM employees
                WHERE department = ?
                ORDER BY id
                """;

        return jdbcTemplate.query(sql, this::mapEmployee, department);
    }

    public SalaryReport getSalaryReport() {
        String sql = """
                SELECT COUNT(*) AS total_employees,
                       COALESCE(SUM(salary), 0) AS total_salary,
                       COALESCE(AVG(salary), 0) AS average_salary,
                       COALESCE(MAX(salary), 0) AS highest_salary,
                       COALESCE(MIN(salary), 0) AS lowest_salary
                FROM employees
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new SalaryReport(
                rs.getLong("total_employees"),
                rs.getDouble("total_salary"),
                rs.getDouble("average_salary"),
                rs.getDouble("highest_salary"),
                rs.getDouble("lowest_salary")
        ));
    }
}
