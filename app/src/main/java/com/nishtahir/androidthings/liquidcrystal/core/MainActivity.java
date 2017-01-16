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

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.nishtahir.androidthings.liquidcrystal.io.LiquidCrystal;
import com.nishtahir.androidthings.liquidcrystal.network.api.WeatherService;

import java.io.IOException;

import javax.inject.Inject;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    WeatherService weatherService;

    @Nullable
    private LiquidCrystal liquidCrystal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LiquidCrystalApplication.get(this).getComponent().inject(this);

        PeripheralManagerService service = new PeripheralManagerService();

        Log.d(TAG, Build.DEVICE);
        Log.d(TAG, "Available GPIO: " + service.getGpioList());

        try {
            Gpio rs = service.openGpio("BCM6");
            Gpio e = service.openGpio("BCM19");
            Gpio d4 = service.openGpio("BCM26");
            Gpio d5 = service.openGpio("BCM16");
            Gpio d6 = service.openGpio("BCM20");
            Gpio d7 = service.openGpio("BCM21");


            liquidCrystal = new LiquidCrystal(rs, e, d4, d5, d6, d7);
            liquidCrystal.write((byte) 0xFF);

//            weatherService.loadCurrentWeather("Charlottesville VA", new Callback<CurrentWeather>() {
//                @Override
//                public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
//                    response.body().main().currentTemp();
//                    CurrentWeather weather = response.body();
//                    try {
//                        liquidCrystal.write(weather.name() + " " + weather.main().currentTemp());
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CurrentWeather> call, Throwable t) {
//                    Log.e(TAG, t.getMessage());
//                }
//            });

        } catch (IOException | InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (liquidCrystal != null) {
                liquidCrystal.shutDown();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
