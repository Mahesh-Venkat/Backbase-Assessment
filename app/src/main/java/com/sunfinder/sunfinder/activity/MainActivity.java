package com.sunfinder.sunfinder.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.fragments.CitiesFragment;
import com.sunfinder.sunfinder.fragments.CityFragment;
import com.sunfinder.sunfinder.transferobject.CityInfoTO;
import com.sunfinder.sunfinder.transferobject.WeatherInfoTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CitiesFragment.OnCitySelectedListener {

    SharedPreferences mSharedPreferences;

    private WeatherInfoTO mWeatherInfoTO;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_weather);
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
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network Connectivity", Toast.LENGTH_LONG).show();
        }
    }

    private String getCityInfoObjectString(int position) {
        CityInfoTO cityInfoTO = getCity(position);

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

            editor.putString(getString(R.string.shared_preference_key), getCititesJSONArrayInString());
            editor.commit();
        }

    }

    private String getCititesJSONArrayInString(){

        List<CityInfoTO> cityInfoList = new ArrayList<>();
        cityInfoList.add(getAmsterdamCityTO());
        cityInfoList.add(getAtlantaCityTO());
        cityInfoList.add(getHyderabadCityTO());
        cityInfoList.add(getLondonCityTO());
        cityInfoList.add(getNewYorkCityTO());

        Gson gson = new Gson();
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();
        String json = gson.toJson(cityInfoList, type);

        Log.d("Cities string Json", json);

        return json;
    }

    private CityInfoTO getAmsterdamCityTO() {
        CityInfoTO amsterdamInfo = new CityInfoTO();

        amsterdamInfo.setCityName("Amsterdam");
        amsterdamInfo.setLatitude(52.371481);
        amsterdamInfo.setLongitude(4.927874);

        return amsterdamInfo;
    }

    private CityInfoTO getAtlantaCityTO() {
        CityInfoTO atlantaInfo = new CityInfoTO();

        atlantaInfo.setCityName("Atlanta");
        atlantaInfo.setLatitude(33.749366);
        atlantaInfo.setLongitude(-84.389030);

        return atlantaInfo;
    }

    private CityInfoTO getHyderabadCityTO() {
        CityInfoTO hyderabadInfo = new CityInfoTO();

        hyderabadInfo.setCityName("Hyderabad");
        hyderabadInfo.setLatitude(17.383323);
        hyderabadInfo.setLongitude(78.450562);

        return hyderabadInfo;
    }

    private CityInfoTO getLondonCityTO() {
        CityInfoTO londonInfo = new CityInfoTO();

        londonInfo.setCityName("London");
        londonInfo.setLatitude(51.526599);
        londonInfo.setLongitude(-0.123625);

        return londonInfo;
    }

    private CityInfoTO getNewYorkCityTO() {
        CityInfoTO newyorkInfo = new CityInfoTO();

        newyorkInfo.setCityName("Newyork");
        newyorkInfo.setLatitude(40.718843);
        newyorkInfo.setLongitude(-74.009632);

        return newyorkInfo;
    }

    private CityInfoTO getCity(int position) {
        Gson gson = new Gson();

        String json = mSharedPreferences.getString(getString(R.string.shared_preference_key),"");
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();

        List<CityInfoTO> cityInfoTOList = gson.fromJson(json, type);

        return cityInfoTOList.get(position);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
