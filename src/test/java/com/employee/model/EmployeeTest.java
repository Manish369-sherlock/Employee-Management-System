package com.employee.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testEmployeeCreation() {

        Employee employee = new Employee(
                "Rohan",
                "rohan@test.com",
                "9876543210",
                "IT",
                30000
        );

        assertEquals("Rohan", employee.getName());
        assertEquals("rohan@test.com", employee.getEmail());
        assertEquals("9876543210", employee.getPhone());
        assertEquals("IT", employee.getDepartment());
        assertEquals(30000, employee.getSalary());
    }

    @Test
    void testEmployeeSetters() {

        Employee employee = new Employee();

        employee.setId(10);
        employee.setName("Anita");
        employee.setEmail("anita@test.com");
        employee.setPhone("9876000000");
        employee.setDepartment("HR");
        employee.setSalary(40000);

        assertEquals(10, employee.getId());
        assertEquals("Anita", employee.getName());
        assertEquals("anita@test.com", employee.getEmail());
        assertEquals("9876000000", employee.getPhone());
        assertEquals("HR", employee.getDepartment());
        assertEquals(40000, employee.getSalary());
    }

    @Test
    void testEmployeeWithId() {

        Employee employee = new Employee(
                5,
                "Ramesh",
                "ramesh@test.com",
                "9876111111",
                "Finance",
                35000
        );

        assertEquals(5, employee.getId());
        assertEquals("Ramesh", employee.getName());
        assertEquals("Finance", employee.getDepartment());
        assertEquals(35000, employee.getSalary());
    }

    @Test
    void testSalary() {

        Employee employee = new Employee();

        employee.setSalary(50000);

        assertEquals(50000, employee.getSalary());
    }
}