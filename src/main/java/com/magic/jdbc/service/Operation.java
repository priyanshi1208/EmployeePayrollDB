package com.magic.jdbc.service;

import com.magic.jdbc.entity.EmployeePayroll;
import com.magic.jdbc.utility.JDBCConnection;
import com.mysql.cj.jdbc.JdbcConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Operation {
  JDBCConnection jdbcConnection=new JDBCConnection();
  public List<EmployeePayroll> retrieveData(){
      try {
          ResultSet resultSet=jdbcConnection.createConnection("Select * from payrollService")
                  .executeQuery();
          List<EmployeePayroll> list=new ArrayList<>();
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
}

