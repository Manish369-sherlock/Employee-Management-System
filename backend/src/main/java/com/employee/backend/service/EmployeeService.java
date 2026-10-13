package com.employee.backend.service;

import com.employee.backend.dao.EmployeeDAO;
import com.employee.backend.model.Employee;
import com.employee.backend.model.SalaryReport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    public Optional<Employee> getEmployeeById(int id) {
        return employeeDAO.getEmployeeById(id);
    }

    public Employee addEmployee(Employee employee) {
        return employeeDAO.addEmployee(employee);
    }

    public boolean updateEmployee(int id, Employee employee) {
        return employeeDAO.updateEmployee(id, employee);
    }

    public boolean deleteEmployee(int id) {
        return employeeDAO.deleteEmployee(id);
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeDAO.getEmployeesByDepartment(department);
    }

    public SalaryReport getSalaryReport() {
        return employeeDAO.getSalaryReport();
    }
}