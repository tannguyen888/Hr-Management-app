package com.group.hrapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private Department department;
    private double hourlyRate;
    private String email;
    private String phone;

    private List<Attendance> attendanceList;
    private List<Leave> leaveList;

    public Employee(int id, String name, Department department, double hourlyRate, String email, String phone) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.hourlyRate = hourlyRate;
        this.email = email;
        this.phone = phone;

        this.attendanceList = new ArrayList<>();
        this.leaveList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public List<Leave> getLeaveList() {
        return leaveList;
    }

    public void setLeaveList(List<Leave> leaveList) {
        this.leaveList = leaveList;
    }

    public void addAttendance(Attendance a) {
        this.attendanceList.add(a);
    }

    public void addLeave(Leave l) {
        this.leaveList.add(l);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department=" + (department != null ? department.getName() : "N/A") +
                ", hourlyRate=" + hourlyRate +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", attendanceList=" + attendanceList.size() +
                ", leaveList=" + leaveList.size() +
                '}';
    }
}
