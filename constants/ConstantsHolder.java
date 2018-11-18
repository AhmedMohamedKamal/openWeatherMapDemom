package com.demo.ahmed.weather.constants;

/**
 * Created by AhmedKamal on 17/11/2018.
 */

public class ConstantsHolder {
    public static String LogTag="Kamal";

    private static final String WeatherMapAPIKey="ea574594b9d36ab688642d5fbeab847e";
//    http://api.openweathermap.org/data/2.5/find?lat=55.5&lon=37.5&cnt=10
    public static final String WeatherMapURL="http://api.openweathermap.org/data/2.5/find?APPID="+WeatherMapAPIKey+"&";
}
