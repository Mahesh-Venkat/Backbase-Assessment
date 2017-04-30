package com.sunfinder.sunfinder.transferobject;

/**
 * Created by Maahi on 4/29/17.
 */

public class CityInfoTO {
    double latitude;
    double longitude;
    String cityName;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
