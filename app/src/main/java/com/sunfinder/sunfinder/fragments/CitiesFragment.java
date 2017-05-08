package com.sunfinder.sunfinder.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.adapter.CitiesAdapter;
import com.sunfinder.sunfinder.transferobject.CityInfoTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CitiesFragment extends ListFragment {
    OnCitySelectedListener mCallback;

    SharedPreferences mSharedPreferences;

    public interface OnCitySelectedListener {
        public void onCitySelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        setHasOptionsMenu(true);
        setListAdapter(new CitiesAdapter(getContext(), getCities()));
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cities_action_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_add_place) {
            if (isNetworkAvailable()) {
                addMapFragment();
            } else {
                Toast.makeText(getActivity(), "Please check your network Connectivity", Toast.LENGTH_LONG).show();
            }
        } else if (item.getItemId() == R.id.option_help) {
            addHelpFragment();
        }
        return true;
    }

    private void addMapFragment() {
        AddPlaceFragment addPlaceFragment = new AddPlaceFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.cities_fragment_container, addPlaceFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addHelpFragment() {
        HelpFragment helpFragment = new HelpFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.cities_fragment_container, helpFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private List<String> getCities() {
        Gson gson = new Gson();

        String json = mSharedPreferences.getString(getString(R.string.shared_preference_key),"");
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();

        List<CityInfoTO> cityInfoTOList = gson.fromJson(json, type);

        List<String> citiesList = new ArrayList<>();

        for (CityInfoTO cityInfoTO: cityInfoTOList) {
            citiesList.add(cityInfoTO.getCityName());
        }

        return citiesList;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnCitySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
        mCallback.onCitySelected(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        setListAdapter(new CitiesAdapter(getContext(), getCities()));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}