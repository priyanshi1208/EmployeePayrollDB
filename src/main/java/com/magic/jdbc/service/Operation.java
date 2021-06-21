package com.magic.jdbc.service;
import com.magic.jdbc.entity.EmployeePayroll;
import com.magic.jdbc.utility.JDBCConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Operation {
    List<EmployeePayroll> list=new ArrayList<>();
  JDBCConnection jdbcConnection=new JDBCConnection();
  public List<EmployeePayroll> retrieveData(){
      try {
          ResultSet resultSet=jdbcConnection.createConnection("Select * from payrollService")
                  .executeQuery();
          while(resultSet.next()){
              list.add(new EmployeePayroll(resultSet.getInt("id"),resultSet.getString("name"),
                      resultSet.getString("salary"),resultSet.getString("startDate")));
          }
          return list;
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
      return null;
  }
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
      try {
          retrieveData();
          updateSalary(name);
          PreparedStatement preparedStatement1=jdbcConnection.getConnection().prepareStatement("select * from payrollService where name=?");
          preparedStatement1.setString(1,name);
          ResultSet resultSet=preparedStatement1.executeQuery();
          if(resultSet.next()){
              list.forEach(employeePayroll -> {
                  if (employeePayroll.getName().equals(name)) {
                      try {
                          employeePayroll.setSalary(resultSet.getString("salary"));
                      } catch (SQLException throwables) {
                          throwables.printStackTrace();
                      }
                  } });
          }return list;
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }return null;
  }
}

