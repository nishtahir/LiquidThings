package com.nishtahir.androidthings.liquidcrystal.network.api.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 *
 */
@AutoValue
public abstract class Weather {

    @SerializedName("id")
    public abstract double id();

    @SerializedName("main")
    public abstract String main();

    @SerializedName("description")
    public abstract String description();

    @SerializedName("icon")
    public abstract String icon();

    @NonNull
    public Weather create(double id, String main, String description, String icon) {
        return new AutoValue_Weather(id, main, description, icon);
    }
    public static TypeAdapter<Weather> typeAdapter(Gson gson) {
        return new AutoValue_Weather.GsonTypeAdapter(gson);
    }
}