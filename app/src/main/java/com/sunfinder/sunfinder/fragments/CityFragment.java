package com.sunfinder.sunfinder.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.adapter.ForecastedWeatherAdapter;
import com.sunfinder.sunfinder.httpclient.CityWeatherclient;
import com.sunfinder.sunfinder.transferobject.CityInfoTO;
import com.sunfinder.sunfinder.transferobject.CityWeatherInfoTO;
import com.sunfinder.sunfinder.transferobject.WeatherInfoTO;

import java.lang.reflect.Type;
import java.util.List;

public class CityFragment extends Fragment {

    public final static String ARG_CITYINFO = "CityInfoTO";
    private String cityInfoTOString;
    private TextView cityTextView;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView rainChancesTextView;
    private TextView windTextView;
    private ListView fiveDaysWeatherListView;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            cityInfoTOString = savedInstanceState.getString(ARG_CITYINFO);
        }

        View view = inflater.inflate(R.layout.city_weather_view, container, false);

        cityTextView = (TextView) view.findViewById(R.id.text_city_name);
        temperatureTextView = (TextView) view.findViewById(R.id.text_temperature);
        humidityTextView = (TextView) view.findViewById(R.id.text_humidity);
        rainChancesTextView = (TextView) view.findViewById(R.id.text_rain_chances);
        windTextView = (TextView) view.findViewById(R.id.text_wind);
        fiveDaysWeatherListView = (ListView) view.findViewById(R.id.list_five_days_climate);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            updateCityView(args.getString("CityInfoTO"));
        } else if (cityInfoTOString != null || !cityInfoTOString.isEmpty()) {
            updateCityView(cityInfoTOString);
        }
    }

    public void updateCityView(String cityInfoTO) {
        GetCityWeatherTask getCityWeatherTask = new GetCityWeatherTask();
        getCityWeatherTask.execute(cityInfoTO);

        cityInfoTOString = cityInfoTO;
    }

    private CityInfoTO getCityInfoTOFromString(String cityInfo) {
        Gson gson = new Gson();

        Type type = new TypeToken<CityInfoTO>() {}.getType();

        CityInfoTO cityInfoTO = gson.fromJson(cityInfo, type);

        return  cityInfoTO;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putString(ARG_CITYINFO, cityInfoTOString);
    }

    private class GetCityWeatherTask extends AsyncTask<String, Void, CityWeatherInfoTO> {
        CityInfoTO cityInfoTO;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected CityWeatherInfoTO doInBackground(String... urls) {

            CityWeatherclient client = new CityWeatherclient();

            cityInfoTO = getCityInfoTOFromString(urls[0]);

            WeatherInfoTO weatherInfoTO = client.getCityTodaysWeather(getActivity(), Double.toString(cityInfoTO.getLatitude()), Double.toString(cityInfoTO.getLongitude()));

            List<WeatherInfoTO> forcastedWeather = client.getCityForecastedWeather(getActivity(), Double.toString(cityInfoTO.getLatitude()), Double.toString(cityInfoTO.getLongitude()));

            CityWeatherInfoTO cityWeatherInfoTO = new CityWeatherInfoTO();
            cityWeatherInfoTO.setForcastedWeather(forcastedWeather);
            cityWeatherInfoTO.setTodaysWeather(weatherInfoTO);

            return cityWeatherInfoTO;
        }

        @Override
        protected void onPostExecute(CityWeatherInfoTO cityWeatherInfoTO) {

            WeatherInfoTO weatherInfoTO = cityWeatherInfoTO.getTodaysWeather();
            progressBar.setVisibility(View.INVISIBLE);

            if (weatherInfoTO != null) {
                //Update UI here with weathInfoTO
                cityTextView.setText(cityInfoTO.getCityName());
                int temperature = (int) Math.floor(weatherInfoTO.getMain().getTemp() + 0.5d);
                temperatureTextView.setText(Integer.toString(temperature) + " \u2103");
                humidityTextView.setText("Humidity: " + Double.toString(weatherInfoTO.getMain().getHumidity()) + "%");
                rainChancesTextView.setText("Rain Chances: " + weatherInfoTO.getWeather().get(0).getDescription());
                windTextView.setText("Wind: " + Double.toString(weatherInfoTO.getWind().getSpeed()) + "km/h");
            }

            List<WeatherInfoTO> forecastedWeather = cityWeatherInfoTO.getForcastedWeather();
            if (forecastedWeather != null) {
                fiveDaysWeatherListView.setDivider(null);
                fiveDaysWeatherListView.setDividerHeight(0);
                fiveDaysWeatherListView.setAdapter(new ForecastedWeatherAdapter(getContext(),forecastedWeather ));
            }

        }
    }
}