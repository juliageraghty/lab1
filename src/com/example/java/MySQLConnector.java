package com.example.java;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MySQLConnector {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/stock?useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "solstice123";
    static final ArrayList<StockInfo> stockInfoArrayList = new ArrayList<>();

    public static void main(String[] args) throws ParseException {
        getConnection();
        queryByDay("2018-06-25");

        for (StockInfo aStockInfoArrayList : stockInfoArrayList) {
            System.out.print("SYMBOL: " + aStockInfoArrayList.symbol + " ");
            System.out.print("HIGHEST: " + aStockInfoArrayList.max + " ");
            System.out.print("LOWEST: " + aStockInfoArrayList.min + " ");
            System.out.print("TOTAL VOLUME: " + aStockInfoArrayList.sum + " " + "\n");
        }
    }


    private static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    static void insertRow(String symbol, Double price, String dateString, Integer volume) {
        Connection conn = getConnection();
        String query = " insert into stockTable (symbol, price, date, volume) "
               + "values (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, symbol);
            stmt.setDouble(2, price);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            java.util.Date formatDate = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(formatDate.getTime());
            stmt.setDate(3, sqlDate);
            stmt.setDouble(4, volume);
            stmt.execute();
            conn.close();
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void queryByDay(String inputDate) throws ParseException {
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
            saveStockInfo(rs);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveStockInfo(ResultSet rs) throws SQLException {
        while (rs.next()) {
            String symbol = rs.getString("SYMBOL");
            Double max = rs.getDouble("MAX(PRICE)");
            Double min = rs.getDouble("MIN(PRICE)");
            Double sum = rs.getDouble("totalVolume");
            StockInfo myStock = new StockInfo(symbol, max, min, sum);
            stockInfoArrayList.add(myStock);
        }
    }

}

