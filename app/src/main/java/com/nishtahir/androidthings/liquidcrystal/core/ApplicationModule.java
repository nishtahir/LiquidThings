package com.nishtahir.androidthings.liquidcrystal.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @NonNull
    private final Application application;

    public ApplicationModule(@NonNull Application application) {
        this.application = application;
    }

    @Provides
    @NonNull
    @Singleton
    public Application provideApplication() {
        return this.application;
    }

    @Provides
    @NonNull
    @Singleton
    public Context provideContext(@NonNull Application application) {
        return application;
    }

}
