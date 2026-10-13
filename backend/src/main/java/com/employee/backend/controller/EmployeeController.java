package com.employee.backend.controller;

import com.employee.backend.model.Employee;
import com.employee.backend.model.SalaryReport;
import com.employee.backend.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) String department) {
        if (department != null && !department.isBlank() && !department.equalsIgnoreCase("All")) {
            return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department));
        }
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(
            @PathVariable String department) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department));
    }

    @GetMapping("/salary-report")
    public ResponseEntity<SalaryReport> getSalaryReport() {
        return ResponseEntity.ok(employeeService.getSalaryReport());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable int id) {

        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(
            @Valid @RequestBody Employee employee) {

        Employee savedEmployee = employeeService.addEmployee(employee);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEmployee(
            @PathVariable int id,
            @Valid @RequestBody Employee employee) {

        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        boolean updated =
                employeeService.updateEmployee(id, employee);

        return updated
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable int id) {

        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        boolean deleted = employeeService.deleteEmployee(id);

        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
