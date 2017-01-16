package com.nishtahir.androidthings.liquidcrystal.network.api.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Coordinates {

    @SerializedName("lat")
    public abstract double latitude();

    @SerializedName("lon")
    public abstract double longitude();

    public static Coordinates create(double latitude, double longitude) {
        return new AutoValue_Coordinates(latitude, longitude);
    }
    public static TypeAdapter<Coordinates> typeAdapter(Gson gson) {
        return new AutoValue_Coordinates.GsonTypeAdapter(gson);
    }
}