package com.sunfinder.sunfinder.transferobject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maahi on 4/29/17.
 */

public class WeatherInfoTO {
    private MainTO main = new MainTO();
    private List<WeatherTO> weather= new ArrayList<>();
    private WindTO wind = new WindTO();

    public MainTO getMain() {
        return main;
    }

    public void setMain(MainTO main) {
        this.main = main;
    }

    public List<WeatherTO> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherTO> weather) {
        this.weather = weather;
    }

    public WindTO getWind() {
        return wind;
    }

    public void setWind(WindTO wind) {
        this.wind = wind;
    }
}
