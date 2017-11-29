package com.muelpatmore.googlemapsdemo.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.muelpatmore.googlemapsdemo.network.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Samuel on 29/11/2017.
 */

public class ServerConnection {
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    public static RequestInterface getJustEatServerConnection() {

        okHttpClient= new OkHttpClient.Builder().
                addInterceptor(new HttpLoggingInterceptor()).build();

        retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.JUST_EAT_BASE_URL)
            .client(okHttpClient)
            .build();

        return retrofit.create(RequestInterface.class);
    }

    public static RequestInterface getGoogleServerConnection() {

        okHttpClient= new OkHttpClient.Builder().
                addInterceptor(new HttpLoggingInterceptor()).build();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.GOOGLE_BASE_USL)
                .client(okHttpClient)
                .build();

        return retrofit.create(RequestInterface.class);
    }

    public static RequestInterface getPostcodeServerConnection() {

        okHttpClient= new OkHttpClient.Builder().
                addInterceptor(new HttpLoggingInterceptor()).build();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.POSTCODE_BASE_URL)
                .client(okHttpClient)
                .build();

        return retrofit.create(RequestInterface.class);
    }
}
