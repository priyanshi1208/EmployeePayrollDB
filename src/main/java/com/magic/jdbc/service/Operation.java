package com.magic.jdbc.service;

import com.magic.jdbc.entity.EmployeePayroll;
import com.magic.jdbc.entity.EmployeePayrollDetails;
import com.magic.jdbc.utility.JDBCConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Operation {
    List<EmployeePayroll> list = new ArrayList<>();

    JDBCConnection jdbcConnection = new JDBCConnection();

    public List<EmployeePayrollDetails> updateSalary(int id, double salary) {
        long start = System.currentTimeMillis();

//        Runnable task = () -> {
            int rowsaffected = -1;
            try {
                rowsaffected = jdbcConnection.createConnection
                        (String.format("Update payrollService set salary=%s where id=%d ", salary, id)).executeUpdate();
                double deductions = salary * 0.2;
                double taxablePay = salary - deductions;
                double tax = taxablePay * 0.1;
                double netPay = salary - tax;
                if (rowsaffected == 1)
                    jdbcConnection.createConnection(String.format("Update payrollDetails set basicPay=%s," +
                            "deductions=%s,taxablePay=%s,tax=%s,netPay=%s  where employeeId=%d", salary, deductions, taxablePay, tax, netPay, id)).executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
//        };
        return retrieveFromPayrollDetailsDb();
    }

    public List<EmployeePayroll> employeeWithinDateRange(LocalDate date1, LocalDate date2) {
        String sql = String.format("select * from payrollService where startDate between '%s' and '%s'",
                Date.valueOf(date1), Date.valueOf(date2));
        try {
            ResultSet resultSet = jdbcConnection.createConnection(sql).executeQuery();
            while (resultSet.next()) {
                list.add(new EmployeePayroll(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getDouble("salary"), resultSet.getString("startDate"),
                        resultSet.getString("gender")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public List<EmployeePayroll> retrieveFromPayrollServiceDb() {
        try {
            ResultSet resultSet = jdbcConnection.createConnection("Select * from payrollService").executeQuery();
            while (resultSet.next()) {
                list.add(new EmployeePayroll(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getDouble("salary"), resultSet.getString("startDate"),
                        resultSet.getString("gender")));
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<EmployeePayrollDetails> retrieveFromPayrollDetailsDb() {
        List<EmployeePayrollDetails> list1 = new ArrayList<>();
        try {
            ResultSet resultSet = jdbcConnection.createConnection("Select * from payrollDetails").executeQuery();
            while (resultSet.next()) {
                list1.add(new EmployeePayrollDetails(resultSet.getDouble("deductions"),
                        resultSet.getDouble("taxablePay"),
                        resultSet.getDouble("tax"), resultSet.getDouble("netPay"),
                        resultSet.getDouble("basicPay")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list1;
    }

    public void addEmployeeToPayroll(int id, String name, double salary, LocalDate startDate, String gender) {
        long start = System.currentTimeMillis();
        try {
            jdbcConnection.createConnection(String.format("Insert into payrollService (id,name,salary,startDate,gender) values ('%s','%s','%s','%s','%s')",
                    id, name, salary, Date.valueOf(startDate), gender)).executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        long stop = System.currentTimeMillis();
        System.out.println(stop - start);
    }

    public void addDataIntoPayrollDetail(int employeeid, double salary) {
        double deductions = salary * 0.2;
        double taxablePay = salary - deductions;
        double tax = taxablePay * 0.1;
        double netPay = salary - tax;
        try {
            jdbcConnection.createConnection(String
                    .format("INSERT INTO payrolldetails(employeeId, basicPay, deductions, taxablePay, tax, netPay)"
                            + "VALUES( %s,%s, %s, %s, %s, %s)", employeeid, salary, deductions, taxablePay, tax, netPay)).executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<EmployeePayroll> addIntoAllTables(int id, String name, double salary, LocalDate
            startDate, String gender) {
        Connection connection = jdbcConnection.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        long start = System.currentTimeMillis();
        System.out.println(start);
        Runnable task = () -> {
            addEmployeeToPayroll(id, name, salary, startDate, gender);
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            addDataIntoPayrollDetail(id, salary);
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                connection.commit();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        System.out.println(thread.getName());
        System.out.println(thread.getState());
        long stop = System.currentTimeMillis();
        System.out.println(stop);
        System.out.println(stop - start);
        return list;
    }

    public static void main(String[] args) {
        Operation operation = new Operation();
        //operation.addIntoAllTables(24,"devu",2200,LocalDate.of(2020,02,02),"male");
        List<EmployeePayrollDetails> employeePayrollDetails = operation.updateSalary(24, 20);
        System.out.println(employeePayrollDetails);


    }
}

