package com.nishtahir.androidthings.liquidcrystal.network.api;

import android.support.annotation.NonNull;

import com.nishtahir.androidthings.liquidcrystal.network.api.model.CurrentWeather;

import retrofit2.Call;
import retrofit2.Callback;

public class WeatherService {

    @NonNull
    private OpenWeatherMapApi api;

    public WeatherService(@NonNull OpenWeatherMapApi api) {
        this.api = api;
    }

    public void loadCurrentWeather(@NonNull String location, @NonNull Callback<CurrentWeather> currentWeatherCallback) {
        Call<CurrentWeather> currentWeatherCall = api.loadCurrent(location);
        currentWeatherCall.enqueue(currentWeatherCallback);
    }
}
