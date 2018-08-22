package com.example.java;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class mySQLConnector {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/stock?useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "solstice123";

    public static void main(String[] args) throws ParseException {
        getConnection();
        refreshTable();
        //getMax("2018-06-22");
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


    public static void insertRow(String symbol, Double price, String dateString, Integer volume) {
        Connection conn = getConnection();
        String query = " insert into stockTable (symbol, price, date, volume) "
               + "values (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, symbol);
            stmt.setDouble(2, price);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date formatDate = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(formatDate.getTime());

            stmt.setDate(3, sqlDate);
            stmt.setDouble(4, volume);
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void refreshTable() {
        Connection conn = getConnection();
        String query = "DELETE from stockTable";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void getMax(String date) throws ParseException {
        Connection conn = getConnection();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date formatDate = format.parse(date);
        java.sql.Date sqlDate = new java.sql.Date(formatDate.getTime());
        String query = "SELECT max(price) from stockTable";
        executeQuery(conn, query);

    }

    private static void executeQuery(Connection conn, String query) {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            String output = null;
            if(rs.next())
                output = rs.getString(1);
            System.out.println(output);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getMin() {
        Connection conn = getConnection();
        String query = "SELECT MIN(price) from stocktable" +
                "WHERE date = \"2018-06-22\";";
        executeQuery(conn, query);
    }

    public static void getTotal() {
        Connection conn = getConnection();
        String query = "SELECT SUM(price) from stocktable" +
                "WHERE date = \"2018-06-22\";";
        executeQuery(conn, query);
    }

}

