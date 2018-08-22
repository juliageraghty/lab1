package com.example.java;

import java.sql.*;

public class mySQLConnector {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/stock?useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "solstice123";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT symbol, price, volume, date FROM stockTable";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                String symbol  = rs.getString("symbol");
                int price = rs.getInt("price");
                int volume = rs.getInt("volume");
                Date date = rs.getDate("date");

                //Display values
                System.out.print("Symbol: " + symbol);
                System.out.print(", Price: " + price);
                System.out.print(", Volume: " + volume);
                System.out.println(", Date: " + date);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }
}

