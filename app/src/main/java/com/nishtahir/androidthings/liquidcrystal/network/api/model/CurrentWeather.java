package com.nishtahir.androidthings.liquidcrystal.network.api.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class CurrentWeather {

    @SerializedName("coord")
    public abstract Coordinates coord();

    @SerializedName("weather")
    public abstract List<Weather> weather();

    @SerializedName("name")
    public abstract String name();

    @SerializedName("main")
    public abstract Main main();

    @SerializedName("cod")
    public abstract int statusCode();

    public CurrentWeather create(Coordinates coord, List<Weather> weather, String name, Main main, int cod) {
        return new AutoValue_CurrentWeather(coord, weather, name, main, cod);
    }

    public static TypeAdapter<CurrentWeather> typeAdapter(Gson gson) {
        return new AutoValue_CurrentWeather.GsonTypeAdapter(gson);
    }
}
