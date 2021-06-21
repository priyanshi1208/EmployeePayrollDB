package com.magic.jdbc.utility;

import java.sql.*;

public class JDBCConnection {
    private String url="jdbc:mysql://localhost:3306/payroll";
    private String userName="root";
    private String password="bunny1234";
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url,userName,password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public PreparedStatement createConnection(String query){
        Connection con=getConnection();
        try {
            return con.prepareStatement(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
