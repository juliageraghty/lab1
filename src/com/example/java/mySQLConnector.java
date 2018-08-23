package com.example.java;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class mySQLConnector {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/stock?useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "solstice123";
    static stockInfo myStock;

    public static void main(String[] args) throws ParseException {
        getConnection();
        //refreshTable();
        queryMax("2018-06-22");
    }


    public static Connection getConnection() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
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



    public static void queryMax(String inputDate) throws ParseException {
        Connection conn = getConnection();
        String query = "SELECT max(price), min(price), sum(price) from stockTable WHERE date = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date formatDate = format.parse(inputDate);
            java.sql.Date sqlDate = new java.sql.Date(formatDate.getTime());
            stmt.setDate(1, sqlDate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String max = rs.getString(1);
                String min = rs.getString(2);
                String sum = rs.getString(3);
                System.out.println("MAX: " + max + " MIN : " + min + " SUM: " + sum);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

