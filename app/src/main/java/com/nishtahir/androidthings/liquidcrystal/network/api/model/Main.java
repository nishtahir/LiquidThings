package com.nishtahir.androidthings.liquidcrystal.network.api.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Main {

    @SerializedName("temp")
    public abstract double currentTemp();

    @SerializedName("temp_min")
    public abstract double tempMin();

    @SerializedName("temp_max")
    public abstract double tempMax();

    @NonNull
    public static Main create(double current, double min, double max) {
        return new AutoValue_Main(current, min, max);
    }

    public static TypeAdapter<Main> typeAdapter(Gson gson) {
        return new AutoValue_Main.GsonTypeAdapter(gson);
    }
}