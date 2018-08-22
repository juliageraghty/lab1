package com.example.java;

import java.sql.*;

public class mySQLConnector {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/stock?useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "solstice123";

    public static void main(String[] args) {
        //refreshQuery();
        getConnection();
    }


    public static Connection getConnection() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    public static void saveJSON(String symbol, Integer price, String date, Integer volume) {
        Connection conn = getConnection();
        String query = " insert into stockTable (symbol, price, date, volume) "
               + "values (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, symbol);
            stmt.setInt(2, price);
            stmt.setString(3, date);
            stmt.setInt(4, volume);
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void refreshQuery() {
        Connection conn = getConnection();
        String query = " DELETE rows FROM stockTable" +
                "WHERE symbol IS NOT NULL";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

