package com.nishtahir.androidthings.liquidcrystal.core;

import com.nishtahir.androidthings.liquidcrystal.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
}
