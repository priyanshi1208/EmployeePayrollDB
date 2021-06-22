package com.magic.jdbc.service;
import com.magic.jdbc.entity.EmployeePayroll;
import com.magic.jdbc.utility.JDBCConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Operation {
    List<EmployeePayroll> list=new ArrayList<>();
  JDBCConnection jdbcConnection=new JDBCConnection();
  public void updateSalary(String name){
      try {
          PreparedStatement preparedStatement=jdbcConnection.createConnection("Update payrollService set salary=300000 where name=?");
          preparedStatement.setString(1,name);
          preparedStatement.executeUpdate();
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
  }
  public List<EmployeePayroll> UpdatedEmployeeObject(String name){
          updateSalary(name);
          String sql = String.format("select * from payrollService where name='%s'", name);
           retrieveFromDb(sql).forEach(employeePayroll -> {
                  if (employeePayroll.getName().equals(name)) {
                      employeePayroll.getSalary();
                  }
           });
      return list;
  }
  public List<EmployeePayroll> employeeWithinDateRange(LocalDate date1,LocalDate date2){
      String sql = String.format("select * from payrollService where startDate between '%s' and '%s'",
              Date.valueOf(date1), Date.valueOf(date2));
      return retrieveFromDb(sql);
  }
  public List<EmployeePayroll> retrieveFromDb(String sql){
      try {
          ResultSet resultSet = jdbcConnection.createConnection(sql).executeQuery();
          while(resultSet.next()){
              list.add(new EmployeePayroll(resultSet.getInt("id"),resultSet.getString("name"),
                      resultSet.getString("salary"),resultSet.getString("startDate"),
                      resultSet.getString("gender")));
          }
          return  list;
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
      return null;
  }
  public List<EmployeePayroll> retrieveTable(){
      return retrieveFromDb("Select * from payrollService");
  }
  public void addEmployeeToPayroll(int id,String name,double salary,LocalDate startDate,String gender){
      try {
          jdbcConnection.createConnection(String.format("Insert into payrollService (id,name,salary,startDate,gender) values ('%s','%s','%s','%s','%s')",
                   id,name,salary,Date.valueOf(startDate),gender)).executeUpdate();
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
  }
  public List<EmployeePayroll> addIntoPayrollDetails(int id, String name, double salary, LocalDate startDate, String gender){
      try {
          jdbcConnection.getConnection().setAutoCommit(false);
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
      addEmployeeToPayroll(id, name, salary, startDate, gender);
      try {
          jdbcConnection.getConnection().rollback();
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
      double deductions = salary * 0.2;
      double taxablePay = salary - deductions;
      double tax = taxablePay * 0.1;
      double netPay = salary - tax;

      try {
          jdbcConnection.createConnection(String
                  .format("INSERT INTO payroll_details(id, basic_pay, deductions, taxable_Pay, tax, net_Pay)"
                          + "VALUES( %s,%s, %s, %s, %s, %s)",id, salary, deductions, taxablePay, tax, netPay)).executeUpdate();
           list = retrieveTable();
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
      try {
          jdbcConnection.getConnection().rollback();
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
      try {
          jdbcConnection.getConnection().commit();
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
      try {
          jdbcConnection.getConnection().close();
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
    return list;
  }
}

