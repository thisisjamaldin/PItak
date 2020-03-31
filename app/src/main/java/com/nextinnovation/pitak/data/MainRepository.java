package com.nextinnovation.pitak.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.nextinnovation.pitak.BuildConfig.BASE_URL;

public class MainRepository {

    private static IMainRepository retrofitService;

    public static IMainRepository getService(){
        if (retrofitService == null)
            retrofitService = buildRetrofit();
        return retrofitService;
    }

    private static IMainRepository buildRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(IMainRepository.class);
    }
}
