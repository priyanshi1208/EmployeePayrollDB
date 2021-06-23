package com.magic.jdbc.entity;

public class EmployeePayroll {
    private int id;
    private String name;
    private double salary;
    private String startDate;
    private String gender;

    public EmployeePayroll(int id, String name, double salary, String startDate,String gender) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
        this.gender=gender;
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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
