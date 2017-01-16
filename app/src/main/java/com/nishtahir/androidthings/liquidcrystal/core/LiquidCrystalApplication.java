package com.nishtahir.androidthings.liquidcrystal.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.nishtahir.androidthings.liquidcrystal.network.NetworkModule;
import com.nishtahir.androidthings.liquidcrystal.network.api.OpenWeatherMapApi;

public class LiquidCrystalApplication extends Application {

    ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildApplicationComponent();
    }

    @NonNull
    private ApplicationComponent buildApplicationComponent() {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(OpenWeatherMapApi.API_ROOT))
                .build();
    }

    public static LiquidCrystalApplication get(@NonNull Context context) {
        return (LiquidCrystalApplication) context.getApplicationContext();
    }

    @NonNull
    public ApplicationComponent getComponent() {
        return component;
    }
}
