package com.example.java;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class mySQLConnector {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/stock?useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "solstice123";
    static final ArrayList<stockInfo> stockInfoArrayList = new ArrayList<stockInfo>();

    public static void main(String[] args) throws ParseException {
        getConnection();
        queryInfo("2018-06-22");

        for (int i = 0; i < stockInfoArrayList.size(); i++) {
            System.out.print("SYMBOL: " + stockInfoArrayList.get(i).symbol + " ");
            System.out.print("MAX: " + stockInfoArrayList.get(i).max + " ");
            System.out.print("MIN: " + stockInfoArrayList.get(i).min + " ");
            System.out.print("TOTAL VOLUME: " + stockInfoArrayList.get(i).sum + " " + "\n");
        }
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


    public static void queryInfo(String inputDate) throws ParseException {
        Connection conn = getConnection();
        String query = "SELECT DISTINCT\n" +
                "\tsymbol, max(price), min(price), round(sum(volume)) AS totalVolume\n" +
                "FROM\n" +
                "\tstockTable\n" +
                "WHERE\n" +
                "\tdate= ?\n" +
                "GROUP BY\n" +
                "\tsymbol;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date formatDate = format.parse(inputDate);
            java.sql.Date sqlDate = new java.sql.Date(formatDate.getTime());
            stmt.setDate(1, sqlDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String symbol = rs.getString("SYMBOL");
                Double max = rs.getDouble("MAX(PRICE)");
                Double min = rs.getDouble("MIN(PRICE)");
                Double sum = rs.getDouble("totalVolume");
                stockInfo myStock = new stockInfo(symbol, max, min, sum);
                stockInfoArrayList.add(myStock);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

