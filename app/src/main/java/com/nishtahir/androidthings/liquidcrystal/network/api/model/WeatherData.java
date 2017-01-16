package com.nishtahir.androidthings.liquidcrystal.network.api.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class WeatherData {

    @SerializedName("dt")
    public abstract double timestamp();

    @SerializedName("temp")
    public abstract Temperature temperature();

    @SerializedName("pressure")
    public abstract double pressure();

    @SerializedName("humidity")
    public abstract double humidity();

    @SerializedName("weather")
    public abstract List<Weather> weather();

    @SerializedName("speed")
    public abstract double speed();

    @SerializedName("deg")
    public abstract double deg();

    @SerializedName("clouds")
    public abstract double clouds();

    @SerializedName("rain")
    public abstract double rain();

    @NonNull
    public WeatherData create(double timestamp, Temperature temperature, double pressure, double humidity, List<Weather> weather, double speed, double deg, double clouds, double rain) {
        return new AutoValue_WeatherData(timestamp, temperature, pressure, humidity, weather, speed, deg, clouds, rain);
    }

    public static TypeAdapter<WeatherData> typeAdapter(Gson gson) {
        return new AutoValue_WeatherData.GsonTypeAdapter(gson);
    }
}
