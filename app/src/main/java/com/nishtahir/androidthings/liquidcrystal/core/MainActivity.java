/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nishtahir.androidthings.liquidcrystal.core;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.androidthings.myproject.R;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.nishtahir.androidthings.liquidcrystal.io.LiquidCrystal;
import com.nishtahir.androidthings.liquidcrystal.network.api.WeatherService;
import com.nishtahir.androidthings.liquidcrystal.network.api.model.CurrentWeather;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String SPLASH_IMAGE = "http://66.media.tumblr.com/1ca84118c53c894c08f45feb1a717c2a/tumblr_mv8byguOhx1r0cgg3o4_r1_400.gif";

    @Inject
    WeatherService weatherService;

    @Nullable
    private LiquidCrystal liquidCrystal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        LiquidCrystalApplication.get(this).getComponent().inject(this);

        ImageView view = (ImageView) findViewById(R.id.image);
        Glide.with(this).load(SPLASH_IMAGE).into(view);

        PeripheralManagerService service = new PeripheralManagerService();

        Log.d(TAG, Build.DEVICE);
        Log.d(TAG, "Available GPIO: " + service.getGpioList());

        try {
            Gpio rs = service.openGpio("BCM6");
            Gpio e  = service.openGpio("BCM19");
            Gpio d4 = service.openGpio("BCM26");
            Gpio d5 = service.openGpio("BCM16");
            Gpio d6 = service.openGpio("BCM20");
            Gpio d7 = service.openGpio("BCM21");


            liquidCrystal = new LiquidCrystal(rs, e, d4, d5, d6, d7);
            weatherService.loadCurrentWeather("Charlottesville VA", new Callback<CurrentWeather>() {
                @Override
                public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                    response.body().main().currentTemp();
                    CurrentWeather weather = response.body();
                    try {
                        liquidCrystal.write(weather.name());
                        liquidCrystal.setCursor(2, 1);
                        liquidCrystal.write("" + weather.main().currentTemp() + "F");
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<CurrentWeather> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });

        } catch (IOException | InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (liquidCrystal != null) {
                liquidCrystal.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
