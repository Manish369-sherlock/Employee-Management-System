
package com.employee.backend.controller;

import com.employee.backend.model.Employee;
import com.employee.backend.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

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
    void shouldReturnAllEmployees() throws Exception {
        when(employeeService.getAllEmployees())
                .thenReturn(List.of(sampleEmployee()));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Employee"));
    }

    @Test
    void shouldReturnEmployeeWhenFound() throws Exception {
        when(employeeService.getEmployeeById(1))
                .thenReturn(Optional.of(sampleEmployee()));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturn404WhenEmployeeIsMissing() throws Exception {
        when(employeeService.getEmployeeById(99))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400ForInvalidEmployeeId() throws Exception {
        mockMvc.perform(get("/api/employees/0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateEmployee() throws Exception {
        when(employeeService.addEmployee(any(Employee.class)))
                .thenReturn(sampleEmployee());

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Test Employee",
                                  "email": "test.employee@example.com",
                                  "phone": "9876543210",
                                  "department": "IT",
                                  "salary": 30000
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldRejectInvalidEmployeeData() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "email": "invalid-email",
                                  "phone": "123",
                                  "department": "",
                                  "salary": 0
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).addEmployee(any(Employee.class));
    }

    @Test
    void shouldUpdateEmployee() throws Exception {
        when(employeeService.updateEmployee(eq(1), any(Employee.class)))
                .thenReturn(true);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Test Employee",
                                  "email": "test.employee@example.com",
                                  "phone": "9876543210",
                                  "department": "IT",
                                  "salary": 35000
                                }
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        when(employeeService.deleteEmployee(1)).thenReturn(true);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }
}
