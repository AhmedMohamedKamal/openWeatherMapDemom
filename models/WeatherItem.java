package com.demo.ahmed.weather.models;


import com.demo.ahmed.weather.helpers.StringHelper;

/**
 * Created by AhmedKamal on 17/11/2018.
 */

public class WeatherItem {

    public WeatherItem(String temp_min, String temp_max, String name) {
        setTemp_min(temp_min);
        setTemp_max(temp_max);
        setname(name);
    }

    public String getTemp_min() {
        return (Double.valueOf(temp_min)-273.15)+"";
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_max() {
        return (Double.valueOf(temp_max)-273.15)+"";
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    private String temp_min, temp_max, name;
}
