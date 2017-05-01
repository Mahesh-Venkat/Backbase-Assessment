package com.sunfinder.sunfinder.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.transferobject.CityInfoTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

public class AddPlaceFragment extends Fragment
        implements OnMapReadyCallback, OnMapClickListener {

    private GoogleMap mMap;
    SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        View view = inflater.inflate(R.layout.add_place, container, false);
        initializeMap();
        return view;
    }

    private void initializeMap() {
        if (mMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_container);
            mapFrag.getMapAsync(this);
        }
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    private void setUpMap() {

        mMap.setOnMapClickListener(this);

        for (CityInfoTO cityInfoTO: getCities()) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(cityInfoTO.getLatitude(),cityInfoTO.getLongitude()))
                    .title(cityInfoTO.getCityName()));
        }

    }

    private List<CityInfoTO> getCities() {
        Gson gson = new Gson();

        String json = mSharedPreferences.getString(getString(R.string.shared_preference_key),"");
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();

        List<CityInfoTO> cityInfoTOList = gson.fromJson(json, type);

        return cityInfoTOList;
    }

    @Override
    public void onMapClick(LatLng point) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        String cityName = "";
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            if (addresses.get(0).getLocality() !=null && !addresses.get(0).getLocality().isEmpty()) {
                cityName = addresses.get(0).getLocality();
            } else {
                cityName = addresses.get(0).getAdminArea();
            }
            mMap.addMarker(new MarkerOptions().position(point)
                    .title(cityName));
            writeNewPlaceTOSharedPReference(cityName, point);
            Toast.makeText(getContext(), cityName + "is Added Successfully", Toast.LENGTH_LONG).show();
        }
        replaceMapFragmentWithCitiesFragment();

    }

    private void replaceMapFragmentWithCitiesFragment() {

        CitiesFragment citiesFragment = new CitiesFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, citiesFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void writeNewPlaceTOSharedPReference(String cityName, LatLng point) {
        List<CityInfoTO> cityInfoList = getCities();
        CityInfoTO cityInfoTo = new CityInfoTO();
        cityInfoTo.setCityName(cityName);
        cityInfoTo.setLatitude(point.latitude);
        cityInfoTo.setLongitude(point.longitude);

        cityInfoList.add(cityInfoTo);

        Gson gson = new Gson();
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();
        String json = gson.toJson(cityInfoList, type);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(getString(R.string.shared_preference_key), json);
        editor.commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            setUpMap();
        }
    }
}
