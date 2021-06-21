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
                      String salary = employeePayroll.getSalary();
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
}

