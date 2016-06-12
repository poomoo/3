// (c)2016 Flipboard Inc, All Rights Reserved.

package com.poomoo.api;


import com.poomoo.api.api.CommApi;
import com.poomoo.api.api.JobApi;
import com.poomoo.api.api.UploadApi;
import com.poomoo.api.api.UserApi;
import com.poomoo.api.api.WxApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

//import okhttp3.logging.HttpLoggingInterceptor;

//import retrofit2.GsonConverterFactory;


public class Network {
    private static UserApi userApi;
    private static CommApi commApi;
    private static JobApi jobApi;
    private static UploadApi uploadApi;
    private static WxApi wxApi;
    //    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static String TAG = "Network";
    public static HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;


    public static UserApi getUserApi() {
        if (userApi == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(level);
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
            clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(clientBuilder.build())
                    .baseUrl(NetConfig.url)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            userApi = retrofit.create(UserApi.class);
        }
        return userApi;
    }

    public static CommApi getCommApi() {
        if (commApi == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(level);
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
            clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(clientBuilder.build())
                    .baseUrl(NetConfig.url)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            commApi = retrofit.create(CommApi.class);
        }
        return commApi;
    }

    public static JobApi getJobApi() {
        if (jobApi == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(level);
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
            clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(clientBuilder.build())
                    .baseUrl(NetConfig.url)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            jobApi = retrofit.create(JobApi.class);
        }
        return jobApi;
    }

    public static UploadApi getUploadApi() {
        if (uploadApi == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
            clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(clientBuilder.build())
                    .baseUrl(NetConfig.url)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            uploadApi = retrofit.create(UploadApi.class);
        }
        return uploadApi;
    }

    public static WxApi getWxApi() {
        if (wxApi == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
            clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(clientBuilder.build())
                    .baseUrl(NetConfig.wxUrl)
                    .addConverterFactory(retrofit2.GsonConverterFactory.create())
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            wxApi = retrofit.create(WxApi.class);
        }
        return wxApi;
    }

}
