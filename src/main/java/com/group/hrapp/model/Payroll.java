package com.group.hrapp.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Payroll implements Serializable {
    private static final long serialVersionUID = 1L;

    private Employee employee;
    private double hourlyRate;
    private double hoursWorked;
    private double overtimeHours;
    private double bonus;
    private double taxRate;
    private LocalDate date;

    public Payroll(Employee employee, double hourlyRate, double hoursWorked, double overtimeHours, double bonus,
            double taxRate, LocalDate date) {
        this.employee = employee;
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.overtimeHours = overtimeHours;
        this.bonus = bonus;
        this.taxRate = taxRate;
        this.date = date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public double getBonus() {
        return bonus;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public LocalDate getDate() {
        return date;
    }

    public double calculateGrossPay() {
        return (hoursWorked + 1.5 * overtimeHours) * hourlyRate + bonus;
    }

    public double calculateTax() {
        return calculateGrossPay() * taxRate;
    }

    public double calculateNetPay() {
        return calculateGrossPay() - calculateTax();
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
