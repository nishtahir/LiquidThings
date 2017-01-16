package com.nishtahir.androidthings.liquidcrystal.network.api.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class Forecast {

    @SerializedName("city")
    public abstract City city();

    @SerializedName("cod")
    public abstract String statusCode();

    @SerializedName("message")
    public abstract double message();

    @SerializedName("cnt")
    public abstract double count();

    @SerializedName("list")
    public abstract List<WeatherData> weatherDataList();

    public Forecast create(City city, String statusCode, double message, double count, List<WeatherData> weatherList) {
        return new AutoValue_Forecast(city, statusCode, message, count, weatherList);
    }
    public static TypeAdapter<Forecast> typeAdapter(Gson gson) {
        return new AutoValue_Forecast.GsonTypeAdapter(gson);
    }
}
