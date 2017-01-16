package com.nishtahir.androidthings.liquidcrystal.network.api;

import com.example.androidthings.myproject.BuildConfig;
import com.nishtahir.androidthings.liquidcrystal.network.api.model.CurrentWeather;
import com.nishtahir.androidthings.liquidcrystal.network.api.model.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapApi {

    String API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY;
    String API_ROOT = "http://api.openweathermap.org/";

    String units = "imperial";

    @GET("/data/2.5/forecast/daily?mode=json&units=" + units + "&cnt=14&appid=" + API_KEY)
    Call<Forecast> loadForecast(@Query("q") String location);


    @GET("/data/2.5/weather?mode=json&units=" + units + "&appid=" + API_KEY)
    Call<CurrentWeather> loadCurrent(@Query("q") String location);
}
