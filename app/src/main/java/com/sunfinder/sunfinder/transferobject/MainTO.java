package com.sunfinder.sunfinder.transferobject;

/**
 * Created by Maahi on 4/29/17.
 */

public class MainTO {
    private double temp;
    private double pressure;
    private double humidity;
    private double temp_main;
    private double temp_max;
    private double sea_level;
    private double grnd_level;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemp_main() {
        return temp_main;
    }

    public void setTemp_main(double temp_main) {
        this.temp_main = temp_main;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public double getSea_level() {
        return sea_level;
    }

    public void setSea_level(double sea_level) {
        this.sea_level = sea_level;
    }

    public double getGrnd_level() {
        return grnd_level;
    }

    public void setGrnd_level(double grnd_level) {
        this.grnd_level = grnd_level;
    }
}
