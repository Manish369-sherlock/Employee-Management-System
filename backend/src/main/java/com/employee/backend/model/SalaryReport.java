package com.employee.backend.model;

public class SalaryReport {

    private long totalEmployees;
    private double totalSalary;
    private double averageSalary;
    private double highestSalary;
    private double lowestSalary;

    public SalaryReport() {
    }

    public SalaryReport(long totalEmployees, double totalSalary,
                        double averageSalary, double highestSalary,
                        double lowestSalary) {
        this.totalEmployees = totalEmployees;
        this.totalSalary = totalSalary;
        this.averageSalary = averageSalary;
        this.highestSalary = highestSalary;
        this.lowestSalary = lowestSalary;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public double getHighestSalary() {
        return highestSalary;
    }

    public void setHighestSalary(double highestSalary) {
        this.highestSalary = highestSalary;
    }

    public double getLowestSalary() {
        return lowestSalary;
    }

    public void setLowestSalary(double lowestSalary) {
        this.lowestSalary = lowestSalary;
    }
}
