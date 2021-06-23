package com.magic.jdbc.service;
import com.magic.jdbc.entity.EmployeePayroll;
import com.magic.jdbc.utility.JDBCConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Operation  {
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
                      resultSet.getDouble("salary"),resultSet.getString("startDate"),
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
      long start=System.currentTimeMillis();
      try {
          jdbcConnection.createConnection(String.format("Insert into payrollService (id,name,salary,startDate,gender) values ('%s','%s','%s','%s','%s')",
                   id,name,salary,Date.valueOf(startDate),gender)).executeUpdate();
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
      long stop=System.currentTimeMillis();
      System.out.println(stop-start);
  }
  public List<EmployeePayroll> addIntoPayrollDetails(int id, String name, double salary, LocalDate startDate, String gender){
      Connection connection=jdbcConnection.getConnection();
      try {
          connection.setAutoCommit(false);
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
      long start=System.currentTimeMillis();
      System.out.println(start);
      Runnable task=()->{
          addEmployeeToPayroll(id, name, salary, startDate, gender);
          try {
              connection.rollback();
          } catch (SQLException throwables) {
              throwables.printStackTrace();
          }
          double deductions = salary * 0.2;
          double taxablePay = salary - deductions;
          double tax = taxablePay * 0.1;
          double netPay = salary - tax;

          try {
              jdbcConnection.createConnection(String
                      .format("INSERT INTO payrolldetails(employeeId, basicPay, deductions, taxablePay, tax, netPay)"
                              + "VALUES( %s,%s, %s, %s, %s, %s)",id, salary, deductions, taxablePay, tax, netPay)).executeUpdate();
              list = retrieveTable();
          } catch (SQLException throwables) {
              throwables.printStackTrace();
          }
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
      Thread thread=new Thread(task);
      thread.start();
      System.out.println(thread.getName());
      System.out.println(thread.getState());
      long stop=System.currentTimeMillis();
      System.out.println(stop);
      System.out.println(stop-start);
      return list;
  }

    public static void main(String[] args) {
        Operation operation=new Operation();
        operation.addIntoPayrollDetails(18,"abc",200000,LocalDate.of(2020,02,01),
                "male");
        operation.addIntoPayrollDetails(19,"abc",200000,LocalDate.of(2020,02,01),
                "female");
    }
}

