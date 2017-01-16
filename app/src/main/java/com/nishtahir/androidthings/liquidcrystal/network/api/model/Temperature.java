package com.nishtahir.androidthings.liquidcrystal.network.api.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Temperature {

    @SerializedName("day")
    public abstract double day();

    @SerializedName("min")
    public abstract double min();

    @SerializedName("max")
    public abstract double max();

    @SerializedName("night")
    public abstract double night();

    @SerializedName("eve")
    public abstract double eve();

    @SerializedName("morn")
    public abstract double morn();

    @NonNull
    public Temperature create(double day, double min, double max, double night, double eve, double morn) {
        return new AutoValue_Temperature(day, min, max, night, eve, morn);
    }

    public static TypeAdapter<Temperature> typeAdapter(Gson gson) {
        return new AutoValue_Temperature.GsonTypeAdapter(gson);
    }
}
