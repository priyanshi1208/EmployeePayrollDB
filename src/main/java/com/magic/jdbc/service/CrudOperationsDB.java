package com.magic.jdbc.service;

import com.magic.jdbc.utility.JDBCConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudOperationsDB {
    JDBCConnection jdbcConnection=new JDBCConnection();
    public String crudResults(String sql){
        try {
            ResultSet resultSet= jdbcConnection.createConnection
                    (sql).executeQuery();
            String salary = null;
            while(resultSet.next()){
                 salary = resultSet.getString("salary_output");
            }
            return salary;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }return null;
    }
    public String getSumOfSalary(){
        return crudResults( "SELECT SUM(salary) as salary_output FROM payrollService GROUP BY gender");
    }
    public void getAverageOfSalary(){
        crudResults("Select AVG(salary) as salary_output from payrollService  group by gender");
    }
    public void getMinOfSalary(){
        crudResults("Select MIN(salary) as salary_output from payrollService  group by gender");
    }
    public void getMaxOfSalary(){
        crudResults("Select MAX(salary) as salary_output from payrollService group by gender");
    }
}
