package com.sunfinder.sunfinder.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.transferobject.CityInfoTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maahi on 5/7/17.
 */

public final class Utils {

    public static CityInfoTO getAmsterdamCityTO() {
        CityInfoTO amsterdamInfo = new CityInfoTO();

        amsterdamInfo.setCityName("Amsterdam");
        amsterdamInfo.setLatitude(52.371481);
        amsterdamInfo.setLongitude(4.927874);

        return amsterdamInfo;
    }

    public static CityInfoTO getAtlantaCityTO() {
        CityInfoTO atlantaInfo = new CityInfoTO();

        atlantaInfo.setCityName("Atlanta");
        atlantaInfo.setLatitude(33.749366);
        atlantaInfo.setLongitude(-84.389030);

        return atlantaInfo;
    }

    public static CityInfoTO getHyderabadCityTO() {
        CityInfoTO hyderabadInfo = new CityInfoTO();

        hyderabadInfo.setCityName("Hyderabad");
        hyderabadInfo.setLatitude(17.383323);
        hyderabadInfo.setLongitude(78.450562);

        return hyderabadInfo;
    }

    public static CityInfoTO getLondonCityTO() {
        CityInfoTO londonInfo = new CityInfoTO();

        londonInfo.setCityName("London");
        londonInfo.setLatitude(51.526599);
        londonInfo.setLongitude(-0.123625);

        return londonInfo;
    }

    public static CityInfoTO getNewYorkCityTO() {
        CityInfoTO newyorkInfo = new CityInfoTO();

        newyorkInfo.setCityName("Newyork");
        newyorkInfo.setLatitude(40.718843);
        newyorkInfo.setLongitude(-74.009632);

        return newyorkInfo;
    }

    public static String getCititesJSONArrayInString(){

        List<CityInfoTO> cityInfoList = new ArrayList<>();
        cityInfoList.add(Utils.getAmsterdamCityTO());
        cityInfoList.add(Utils.getAtlantaCityTO());
        cityInfoList.add(Utils.getHyderabadCityTO());
        cityInfoList.add(Utils.getLondonCityTO());
        cityInfoList.add(Utils.getNewYorkCityTO());

        Gson gson = new Gson();
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();
        String json = gson.toJson(cityInfoList, type);

        Log.d("Cities string Json", json);

        return json;
    }

    public static List<CityInfoTO> getCitiesFromSharedPReferences(Context context, SharedPreferences mSharedPreferences) {
        Gson gson = new Gson();

        String json = mSharedPreferences.getString(context.getResources().getString(R.string.shared_preference_key),"");
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();

        List<CityInfoTO> cityInfoTOList = gson.fromJson(json, type);

        return cityInfoTOList;
    }

    public static CityInfoTO getCity(int position, Context context, SharedPreferences mSharedPreferences) {

        List<CityInfoTO> cityInfoTOList = getCitiesFromSharedPReferences(context, mSharedPreferences);

        return cityInfoTOList.get(position);
    }

    public static CityInfoTO getCityInfoTOFromString(String cityInfo) {
        Gson gson = new Gson();

        Type type = new TypeToken<CityInfoTO>() {}.getType();

        CityInfoTO cityInfoTO = gson.fromJson(cityInfo, type);

        return  cityInfoTO;
    }
}
