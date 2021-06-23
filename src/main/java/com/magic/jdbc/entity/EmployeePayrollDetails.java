package com.magic.jdbc.entity;

public class EmployeePayrollDetails {
    private double deductions ;
    private double taxablePay ;
    private double tax ;
    private double netPay ;
    private double basicPay;

    public EmployeePayrollDetails(double deductions, double taxablePay, double tax, double netPay,double basicPay) {
        this.deductions = deductions;
        this.taxablePay = taxablePay;
        this.tax = tax;
        this.netPay = netPay;
        this.basicPay=basicPay;
    }

    @Override
    public String toString() {
        return "EmployeePayrollDetails{" +
                "deductions=" + deductions +
                ", taxablePay=" + taxablePay +
                ", tax=" + tax +
                ", netPay=" + netPay +
                ", basicPay=" + basicPay +
                '}';
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getTaxablePay() {
        return taxablePay;
    }

    public void setTaxablePay(double taxablePay) {
        this.taxablePay = taxablePay;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }
}
