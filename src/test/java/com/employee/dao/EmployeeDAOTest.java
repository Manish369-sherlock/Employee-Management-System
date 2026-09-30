package com.employee.dao;

import com.employee.model.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    @Test
    void testAddEmployee() {

        String email = "junit" + System.currentTimeMillis() + "@test.com";

        Employee employee = new Employee(
                "JUnit Test",
                email,
                "9876500000",
                "Testing",
                25000
        );

        EmployeeDAO dao = new EmployeeDAO();

        assertDoesNotThrow(() -> dao.addEmployee(employee));
    }
}