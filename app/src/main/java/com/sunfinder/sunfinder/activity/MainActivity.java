package com.sunfinder.sunfinder.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.fragments.CitiesFragment;
import com.sunfinder.sunfinder.fragments.CityFragment;
import com.sunfinder.sunfinder.httpclient.CityWeatherclient;
import com.sunfinder.sunfinder.transferobject.CityInfoTO;
import com.sunfinder.sunfinder.transferobject.WeatherInfoTO;
import com.sunfinder.sunfinder.utils.Utils;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity implements CitiesFragment.OnCitySelectedListener {

    SharedPreferences mSharedPreferences;

    private WeatherInfoTO mWeatherInfoTO;
    private TextView cityTextView;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView rainChancesTextView;
    private TextView windTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_cities);
        cityTextView = (TextView) findViewById(R.id.textView_city_name);
        temperatureTextView = (TextView) findViewById(R.id.textView_temperature);
        humidityTextView = (TextView) findViewById(R.id.textView_humidity);
        rainChancesTextView = (TextView) findViewById(R.id.textView_clouds);
        windTextView = (TextView) findViewById(R.id.textView_wind);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            CitiesFragment citiesFragment = new CitiesFragment();

            citiesFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, citiesFragment).commit();
        }

        buildCityInfo();
        GetCityWeatherTask getCityWeatherTask = new GetCityWeatherTask();
        getCityWeatherTask.execute();
    }

    public void onCitySelected(int position) {

        if (isNetworkAvailable()) {

            CityFragment cityFrag = (CityFragment)
                    getSupportFragmentManager().findFragmentById(R.id.article_fragment);

            if (cityFrag != null) {
                cityFrag.updateCityView(getCityInfoObjectString(position));

            } else {
                CityFragment newFragment = new CityFragment();
                Bundle args = new Bundle();
                args.putString("CityInfoTO", getCityInfoObjectString(position));
                newFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.cities_fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network Connectivity", Toast.LENGTH_LONG).show();
        }
    }

    private String getCityInfoObjectString(int position) {
        CityInfoTO cityInfoTO = Utils.getCity(position, getApplicationContext(), mSharedPreferences);

        Gson gson = new Gson();
        Type type = new TypeToken<CityInfoTO>() {}.getType();

        return gson.toJson(cityInfoTO, type);
    }

    private void buildCityInfo() {
        Gson gson = new Gson();

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);

        String cititesInfo = mSharedPreferences.getString(getString(R.string.shared_preference_key),"");
        if (cititesInfo == null || cititesInfo.isEmpty()) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();

            editor.putString(getString(R.string.shared_preference_key), Utils.getCititesJSONArrayInString());
            editor.commit();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class GetCityWeatherTask extends AsyncTask<String, Void, WeatherInfoTO> {
        CityInfoTO cityInfoTO;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected WeatherInfoTO doInBackground(String... urls) {

            CityWeatherclient client = new CityWeatherclient();

            cityInfoTO = Utils.getAtlantaCityTO();

            WeatherInfoTO weatherInfoTO = client.getCityTodaysWeather(getApplicationContext(), Double.toString(cityInfoTO.getLatitude()), Double.toString(cityInfoTO.getLongitude()));

            return weatherInfoTO;
        }

        @Override
        protected void onPostExecute(WeatherInfoTO weatherInfoTO) {


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

        }
    }

}
