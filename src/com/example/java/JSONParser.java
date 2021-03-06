package com.example.java;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {
    static final String URL = "https://bootcamp-training-files.cfapps.io/week1/week1-stocks.json";

    public static void main(String[] args) {
        try {
            URL obj = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            System.out.println("\nSending 'GET' request to URL : " + URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            } in .close();

            JSONArray jsonArray= new JSONArray(response.toString());
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String symbol = jsonObject.getString("symbol");
                Double price= jsonObject.getDouble("price");
                int volume=jsonObject.getInt("volume");
                String date = jsonObject.getString("date");
                MySQLConnector.insertRow(symbol, price, date, volume);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
