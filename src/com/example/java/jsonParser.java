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

public class jsonParser {

    public static void main(String[] args) {
        try {
            String url = "https://bootcamp-training-files.cfapps.io/week1/week1-stocks.json";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in =new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            } in .close();
            //System.out.println(response);


            JSONArray jsonArray= new JSONArray(response.toString());
            System.out.println(jsonArray);
            for(int i=0;i<jsonArray.length();i++){
                //get the JSON Object
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String symbol = jsonObject.getString("symbol");
                int price= jsonObject.getInt("price");
                int volume=jsonObject.getInt("volume");
                String date= jsonObject.getString("date");
                System.out.println(symbol);
                System.out.println(price);
                System.out.println(volume);
                System.out.println(date);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
