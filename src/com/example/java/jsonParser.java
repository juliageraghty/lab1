package com.example.java;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;

public class jsonParser {
    static final String URL = "https://bootcamp-training-files.cfapps.io/week1/week1-stocks.json";

    public static void main(String[] args) {
        try {
            URL obj = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + URL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in =new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            } in .close();

            JSONArray jsonArray= new JSONArray(response.toString());
            for(int i=0;i<4;i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String symbol = jsonObject.getString("symbol");
                int price= jsonObject.getInt("price");
                int volume=jsonObject.getInt("volume");
                //Date dateString = jsonObject.getDate("date");
                //mySQLConnector.saveJSON(symbol, price, dateString, volume);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String formatDate(String date) {
        String[] format = date.split("T");
        String formatDate = format[0];
        return formatDate;
    }
}
