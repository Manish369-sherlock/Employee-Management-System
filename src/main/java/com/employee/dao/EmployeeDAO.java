package com.employee.dao;

import com.employee.model.Employee;
import com.employee.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeDAO {

    public void addEmployee(Employee employee) {

        String sql = "INSERT INTO employees " +
                "(name, email, phone, department, salary) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setString(3, employee.getPhone());
            statement.setString(4, employee.getDepartment());
            statement.setDouble(5, employee.getSalary());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Employee added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error adding employee!");
            e.printStackTrace();
        }
    }

    public void viewAllEmployees() {

        String sql = "SELECT * FROM employees";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Phone: " + resultSet.getString("phone"));
                System.out.println("Department: " + resultSet.getString("department"));
                System.out.println("Salary: " + resultSet.getDouble("salary"));

                System.out.println("----------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving employees!");
            e.printStackTrace();
        }
    }

    public void searchEmployee(int id) {

        String sql = "SELECT * FROM employees WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (var resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    System.out.println("ID: " + resultSet.getInt("id"));
                    System.out.println("Name: " + resultSet.getString("name"));
                    System.out.println("Email: " + resultSet.getString("email"));
                    System.out.println("Phone: " + resultSet.getString("phone"));
                    System.out.println("Department: " + resultSet.getString("department"));
                    System.out.println("Salary: " + resultSet.getDouble("salary"));

                } else {
                    System.out.println("Employee not found!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching employee!");
            e.printStackTrace();
        }
    }

    public void updateEmployee(Employee employee) {

        String sql = "UPDATE employees SET name = ?, email = ?, phone = ?, " +
                "department = ?, salary = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setString(3, employee.getPhone());
            statement.setString(4, employee.getDepartment());
            statement.setDouble(5, employee.getSalary());
            statement.setInt(6, employee.getId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Employee updated successfully!");
            } else {
                System.out.println("Employee not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error updating employee!");
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int id) {

        String sql = "DELETE FROM employees WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Employee not found!");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting employee!");
            e.printStackTrace();
        }
    }

    public void viewEmployeesByDepartment(String department) {

        String sql = "SELECT * FROM employees WHERE department = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, department);

            try (var resultSet = statement.executeQuery()) {

                boolean found = false;

                while (resultSet.next()) {

                    found = true;

                    System.out.println("ID: " + resultSet.getInt("id"));
                    System.out.println("Name: " + resultSet.getString("name"));
                    System.out.println("Email: " + resultSet.getString("email"));
                    System.out.println("Phone: " + resultSet.getString("phone"));
                    System.out.println("Department: " + resultSet.getString("department"));
                    System.out.println("Salary: " + resultSet.getDouble("salary"));

                    System.out.println("----------------------------");
                }

                if (!found) {
                    System.out.println("No employees found in this department!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving employees by department!");
            e.printStackTrace();
        }
    }

    public void salaryReport() {

        String sql = "SELECT COUNT(*) AS total_employees, " +
                "SUM(salary) AS total_salary, " +
                "AVG(salary) AS average_salary, " +
                "MAX(salary) AS highest_salary, " +
                "MIN(salary) AS lowest_salary " +
                "FROM employees";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {

            if (resultSet.next()) {

                System.out.println("\n===== SALARY REPORT =====");
                System.out.println("Total Employees: "
                        + resultSet.getInt("total_employees"));

                System.out.println("Total Salary: "
                        + resultSet.getDouble("total_salary"));

                System.out.println("Average Salary: "
                        + resultSet.getDouble("average_salary"));

                System.out.println("Highest Salary: "
                        + resultSet.getDouble("highest_salary"));

                System.out.println("Lowest Salary: "
                        + resultSet.getDouble("lowest_salary"));

                System.out.println("=========================");
            }

        } catch (SQLException e) {
            System.out.println("Error generating salary report!");
            e.printStackTrace();
        }
    }
}