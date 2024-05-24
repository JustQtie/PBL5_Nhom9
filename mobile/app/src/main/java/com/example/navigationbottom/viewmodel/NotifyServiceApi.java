package com.example.navigationbottom.viewmodel;


import android.content.Context;
import android.util.Log;

import com.example.navigationbottom.model.Notification;

import java.io.IOException;
import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class NotifyServiceApi {
    private NotifyApi api;
    private String authToken;
    public NotifyServiceApi(Context context){
        authToken = SessionManager.getInstance(context).getToken();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + authToken)
                        .build();
                Headers headers = newRequest.headers();
                for (int i = 0, size = headers.size(); i < size; i++) {
                    Log.d("RequestHeader", headers.name(i) + ": " + headers.value(i));
                }
                return chain.proceed(newRequest);
            }
        }).build();

        api = new Retrofit.Builder()
                .client(client)
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(NotifyApi.class);
    }

    public Call<Notification> createNotify(@Body Notification notification){
        return api.createNotify(notification);
    }
    public Call<List<Notification>> getNotify(Long id){
        return api.getNotify(id);
    }
    public Call<Notification> deleteNotify(Long id){
        return api.deleteNotify(id);
    }
}
