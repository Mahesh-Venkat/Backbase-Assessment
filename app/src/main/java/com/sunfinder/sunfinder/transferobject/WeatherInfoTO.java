package com.sunfinder.sunfinder.transferobject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maahi on 4/29/17.
 */

public class WeatherInfoTO {
    private long dt;
    private String dt_txt;
    private MainTO main = new MainTO();
    private List<WeatherTO> weather= new ArrayList<>();
    private WindTO wind = new WindTO();
    private String dayOftheWeek;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

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

    public void setDayOftheWeek(String dayOftheWeek) {
        this.dayOftheWeek = dayOftheWeek;
    }

    public String getDayOftheWeek() {
        return dayOftheWeek;
    }
}
