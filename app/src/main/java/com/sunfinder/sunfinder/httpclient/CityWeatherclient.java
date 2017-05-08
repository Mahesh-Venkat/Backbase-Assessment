package com.sunfinder.sunfinder.httpclient;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.transferobject.WeatherInfoTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CityWeatherclient {
    private Context mContext;

    public WeatherInfoTO getCityTodaysWeather(Context context, String latitude, String longitude, String units) {
        mContext = context;

        try {
            //String currentWeatherUrl = "http://api.openweathermap.org/data/2.5/weather?lat=0&lon=0&appid=c6e381d8c7ff98f0fee43775817cf6ad&units=metric";
            String currentWeatherUrl = buildTodaysWeatherUrl(latitude, longitude, units);
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
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    public List<WeatherInfoTO> getCityForecastedWeather(Context context, String latitude, String longitude, String units) {
        mContext = context;

        try {
            String currentWeatherUrl = buildForcastedWeatherUrl(latitude, longitude, units);
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

                return getForecastedWeatherInfo(stringBuilder.toString());
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    private String buildTodaysWeatherUrl(String latitude, String longitude, String units) {
        String url = mContext.getResources().getString(R.string.weather_api_base_url) + "weather?" + "lat=" + latitude + "&lon=" + longitude + "&appid="
                + mContext.getResources().getString(R.string.weather_api_key) + "&units="+units;

        return url;
    }

    private String buildForcastedWeatherUrl(String latitude, String longitude, String units) {
        String url = mContext.getResources().getString(R.string.weather_api_base_url) + "forecast?" + "lat=" + latitude + "&lon=" + longitude + "&appid="
                + mContext.getResources().getString(R.string.weather_api_key) + "&units=" + units;

        return url;
    }

    private WeatherInfoTO getWeatherInfoTO(String response) {
        Gson gson = new Gson();

        WeatherInfoTO weatherInfoTO = gson.fromJson(response, WeatherInfoTO.class);

        return weatherInfoTO;
    }

    private List<WeatherInfoTO> getForecastedWeatherInfo(String response) {
        try {
            JSONObject result = new JSONObject(response);
            String data = result.getJSONArray("list").toString();
            Gson gson = new Gson();

            Type type = new TypeToken<List<WeatherInfoTO>>() {
            }.getType();

            List<WeatherInfoTO> forecastedWeatherInfoTO = gson.fromJson(data, type);

            return buildFinalForecastList(forecastedWeatherInfoTO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<WeatherInfoTO> buildFinalForecastList(List<WeatherInfoTO> results){
        int prevAddedDate = -1;
        Calendar cal = Calendar.getInstance();
        List<WeatherInfoTO> finalForecastedWeather = new ArrayList<>();
        for(WeatherInfoTO weatherInfoTO : results) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                 date = format.parse(weatherInfoTO.getDt_txt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTimeInMillis(date.getTime());
            int day = cal.get(Calendar.DAY_OF_MONTH);
            if (prevAddedDate != day) {
                String dayOfTheWeek = new SimpleDateFormat("EE").format(date);
                weatherInfoTO.setDayOftheWeek(dayOfTheWeek);
                finalForecastedWeather.add(weatherInfoTO);
            }
            prevAddedDate = day;
        }

        return finalForecastedWeather;
    }


}
