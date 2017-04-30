package com.sunfinder.sunfinder.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.fragments.CitiesFragment;
import com.sunfinder.sunfinder.fragments.CityFragment;
import com.sunfinder.sunfinder.httpclient.CityWeatherclient;
import com.sunfinder.sunfinder.transferobject.CityInfoTO;
import com.sunfinder.sunfinder.transferobject.WeatherInfoTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CitiesFragment.OnCitySelectedListener {

    SharedPreferences mSharedPreferences;
    private List<WeatherInfoTO> mWeatherInfoTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities_weather);
        mWeatherInfoTO = new ArrayList<>();
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            CitiesFragment citiesFragment = new CitiesFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            citiesFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, citiesFragment).commit();
        }

//        GetCityWeatherTask getCityWeatherTask = new GetCityWeatherTask();
//        getCityWeatherTask.execute("");
         buildCityInfo();
    }

    public void onCitySelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        CityFragment articleFrag = (CityFragment)
                getSupportFragmentManager().findFragmentById(R.id.article_fragment);

        if (articleFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            articleFrag.updateCityView(position);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            CityFragment newFragment = new CityFragment();
            Bundle args = new Bundle();
            args.putInt(CityFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    private class GetCityWeatherTask extends AsyncTask<String, Void, WeatherInfoTO> {
        @Override
        protected WeatherInfoTO doInBackground(String... urls) {
            // we use the OkHttp library from https://github.com/square/okhttp
            CityWeatherclient client = new CityWeatherclient();
            WeatherInfoTO weatherInfoTO = client.getCityWeather("0", "0");

            return weatherInfoTO;
        }

        @Override
        protected void onPostExecute(WeatherInfoTO weatherInfoTO) {
            mWeatherInfoTO.add(weatherInfoTO);
        }
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

}
