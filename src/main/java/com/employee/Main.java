package com.employee;

import com.employee.dao.EmployeeDAO;
import com.employee.model.Employee;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        EmployeeDAO employeeDAO = new EmployeeDAO();

        while (true) {

            System.out.println("\n===== EMPLOYEE MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. View Employees by Department");
            System.out.println("7. View Salary Report");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter phone: ");
                    String phone = scanner.nextLine();

                    System.out.print("Enter department: ");
                    String department = scanner.nextLine();

                    System.out.print("Enter salary: ");
                    double salary = scanner.nextDouble();

                    Employee employee = new Employee(
                            name, email, phone, department, salary
                    );

                    employeeDAO.addEmployee(employee);
                    break;

                case 2:
                    employeeDAO.viewAllEmployees();
                    break;

                case 3:
                    System.out.print("Enter employee ID: ");
                    int searchId = scanner.nextInt();

                    employeeDAO.searchEmployee(searchId);
                    break;

                case 4:
                    System.out.print("Enter employee ID: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter new name: ");
                    String updatedName = scanner.nextLine();

                    System.out.print("Enter new email: ");
                    String updatedEmail = scanner.nextLine();

                    System.out.print("Enter new phone: ");
                    String updatedPhone = scanner.nextLine();

                    System.out.print("Enter new department: ");
                    String updatedDepartment = scanner.nextLine();

                    System.out.print("Enter new salary: ");
                    double updatedSalary = scanner.nextDouble();

                    Employee updatedEmployee = new Employee(
                            updateId,
                            updatedName,
                            updatedEmail,
                            updatedPhone,
                            updatedDepartment,
                            updatedSalary
                    );

                    employeeDAO.updateEmployee(updatedEmployee);
                    break;

                case 5:
                    System.out.print("Enter employee ID to delete: ");
                    int deleteId = scanner.nextInt();

                    employeeDAO.deleteEmployee(deleteId);
                    break;

                case 6:
                    System.out.print("Enter department: ");
                    String searchDepartment = scanner.nextLine();

                    employeeDAO.viewEmployeesByDepartment(searchDepartment);
                    break;

                case 7:
                    employeeDAO.salaryReport();
                    break;

                case 8:
                    System.out.println("Thank you for using the application!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}