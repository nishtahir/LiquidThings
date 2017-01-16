package com.nishtahir.androidthings.liquidcrystal.network;

import android.support.annotation.NonNull;

import com.example.androidthings.myproject.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nishtahir.androidthings.liquidcrystal.network.api.AutoValueGson_AutoValueGsonTypeAdapterFactory;
import com.nishtahir.androidthings.liquidcrystal.network.api.OpenWeatherMapApi;
import com.nishtahir.androidthings.liquidcrystal.network.api.WeatherService;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @NonNull
    private HttpUrl endpoint;

    public NetworkModule(@NonNull String endpoint) {
        this(HttpUrl.parse(endpoint));
    }

    public NetworkModule(@NonNull HttpUrl endpoint) {
        this.endpoint = endpoint;
    }

    @Provides
    @NonNull
    @Singleton
    HttpUrl provideEndpoint() {
        return this.endpoint;
    }

    @Provides
    @NonNull
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(AutoValueGson_AutoValueGsonTypeAdapterFactory.create())
                .create();
    }

    @Provides
    @NonNull
    @Singleton
    public OkHttpClient provideHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        }).build();
    }

    @Provides
    @NonNull
    @Singleton
    OpenWeatherMapApi provideApi(@NonNull Gson gson, @NonNull OkHttpClient client, @NonNull HttpUrl url) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .validateEagerly(BuildConfig.DEBUG)
                .build()
                .create(OpenWeatherMapApi.class);
    }


    @Provides
    @NonNull
    WeatherService provideWeatherService(@NonNull OpenWeatherMapApi api) {
        return new WeatherService(api);
    }
}
