package com.demo.ahmed.weather.models;


import com.demo.ahmed.weather.helpers.StringHelper;

/**
 * Created by AhmedKamal on 17/11/2018.
 */

public class LatLng {
    private double lat,lng;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
