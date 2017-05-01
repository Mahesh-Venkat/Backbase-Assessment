package com.sunfinder.sunfinder.transferobject;

import java.util.ArrayList;
import java.util.List;

public class CityWeatherInfoTO {
    private WeatherInfoTO todaysWeather;
    private List<WeatherInfoTO> forcastedWeather = new ArrayList<>();

    public WeatherInfoTO getTodaysWeather() {
        return todaysWeather;
    }

    public void setTodaysWeather(WeatherInfoTO todaysWeather) {
        this.todaysWeather = todaysWeather;
    }

    public List<WeatherInfoTO> getForcastedWeather() {
        return forcastedWeather;
    }

    public void setForcastedWeather(List<WeatherInfoTO> forcastedWeather) {
        this.forcastedWeather = forcastedWeather;
    }
}
