package com.sunfinder.sunfinder.httpclient;

import android.util.Log;

import com.google.gson.Gson;
import com.sunfinder.sunfinder.transferobject.WeatherInfoTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CityWeatherclient {

    public WeatherInfoTO getCityWeather(String latitude, String longitude) {

        try {
            String currentWeatherUrl = "http://api.openweathermap.org/data/2.5/weather?lat=0&lon=0&appid=c6e381d8c7ff98f0fee43775817cf6ad&units=metric";
            URL url = new URL(currentWeatherUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                return getWeatherInfoTO(stringBuilder.toString());
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    private WeatherInfoTO getWeatherInfoTO (String response) {
        Gson gson = new Gson();

        WeatherInfoTO weatherInfoTO = gson.fromJson(response, WeatherInfoTO.class);

        return weatherInfoTO;
    }
}
