package com.nishtahir.androidthings.liquidcrystal.network.api.model;


import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class City {

    @SerializedName("id")
    public abstract String id();

    @SerializedName("name")
    public abstract String name();

    @SerializedName("coord")
    public abstract String coordinates();

    @SerializedName("country")
    public abstract String country();

    @SerializedName("population")
    public abstract String population();

    @NonNull
    public static City create(String id, String name, String coordinates, String country, String population) {
        return new AutoValue_City(id, name, coordinates, country, population);
    }

    public static TypeAdapter<City> typeAdapter(Gson gson) {
        return new AutoValue_City.GsonTypeAdapter(gson);
    }
}