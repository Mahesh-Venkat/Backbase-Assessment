package com.sunfinder.sunfinder.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
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
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            String cityName = addresses.get(0).getAddressLine(0);
            mMap.addMarker(new MarkerOptions().position(point)
                    .title(cityName));
            writeNewPlaceTOSharedPReference(cityName, point);
        }
    }

    private void writeNewPlaceTOSharedPReference(String cityName, LatLng point) {
        List<CityInfoTO> cityInfoList = getCities();
        CityInfoTO cityInfoTo = new CityInfoTO();
        cityInfoTo.setCityName(cityName);
        cityInfoTo.setLatitude(((double) point.latitude));
        cityInfoTo.setLatitude(((double) point.longitude));

        cityInfoList.add(cityInfoTo);

        Gson gson = new Gson();
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();
        String json = gson.toJson(cityInfoList, type);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(getString(R.string.shared_preference_key), json);
        editor.commit();
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            setUpMap();
        }

    }

}
