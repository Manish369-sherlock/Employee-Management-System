package com.employee.dao;

import com.employee.model.Employee;
import com.employee.util.TestDBConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    @Test
    void testAddEmployee() throws Exception {

        String email = "junit" + System.currentTimeMillis() + "@test.com";

        Employee employee = new Employee(
                "JUnit Test",
                email,
                "9876500000",
                "Testing",
                25000
        );

        try (Connection connection = TestDBConnection.getConnection()) {

            EmployeeDAO dao = new EmployeeDAO(connection);

            assertDoesNotThrow(() -> dao.addEmployee(employee));
        }
    }

    @Test
    void testSearchEmployee() throws Exception {

        try (Connection connection = TestDBConnection.getConnection()) {

            EmployeeDAO dao = new EmployeeDAO(connection);

            assertDoesNotThrow(() ->
                    dao.searchEmployee(1)
            );
        }
    }

    @Test
    void testSearchEmployeeNotFound() throws Exception {

        try (Connection connection = TestDBConnection.getConnection()) {

            EmployeeDAO dao = new EmployeeDAO(connection);

            assertDoesNotThrow(() ->
                    dao.searchEmployee(999)
            );
        }
    }

    @Test
    void testUpdateEmployee() throws Exception {

        Employee employee = new Employee(
                1,
                "Rohan Updated",
                "Rohan@gmail.com",
                "9123456780",
                "IT",
                30000
        );

        try (Connection connection = TestDBConnection.getConnection()) {

            EmployeeDAO dao = new EmployeeDAO(connection);

            assertDoesNotThrow(() ->
                    dao.updateEmployee(employee)
            );
        }
    }

    @Test
    void testUpdateEmployeeNotFound() throws Exception {

        Employee employee = new Employee(
                999,
                "Test User",
                "test999@test.com",
                "9000000000",
                "Testing",
                25000
        );

        try (Connection connection = TestDBConnection.getConnection()) {

            EmployeeDAO dao = new EmployeeDAO(connection);

            assertDoesNotThrow(() ->
                    dao.updateEmployee(employee)
            );
        }
    }

    @Test
    void testDeleteEmployee() throws Exception {

        try (Connection connection = TestDBConnection.getConnection()) {

            EmployeeDAO dao = new EmployeeDAO(connection);

            assertDoesNotThrow(() ->
                    dao.deleteEmployee(1)
            );
        }
    }

    @Test
    void testDeleteEmployeeNotFound() throws Exception {

        try (Connection connection = TestDBConnection.getConnection()) {

            EmployeeDAO dao = new EmployeeDAO(connection);

            assertDoesNotThrow(() ->
                    dao.deleteEmployee(999)
            );
        }
    }

    @Test
    void testViewEmployeesByDepartment() throws Exception {

        try (Connection connection = TestDBConnection.getConnection()) {

            EmployeeDAO dao = new EmployeeDAO(connection);

            assertDoesNotThrow(() ->
                    dao.viewEmployeesByDepartment("IT")
            );
        }
    }
}