package com.example.navigationbottom.viewmodel;

import android.content.Context;
import android.util.Log;

import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.LoginResponse;
import com.example.navigationbottom.response.RegisterResponse;
import com.google.gson.Gson;

import java.io.IOException;

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

public class UserApiService {
    private static final String BASE_URL = "https://6355-116-105-167-76.ngrok-free.app/";
    private UserApi api;

    private String authToken;

    public UserApiService(Context context){
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
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(UserApi.class);
    }

    public Call<LoginResponse> postUserLogin(@Body User requestData){
        Log.d("RequestData", new Gson().toJson(requestData));
        return api.postUserLogin(requestData);
    }
    public Call<RegisterResponse> signUpUser(@Body User requestData){
        Log.d("RequestData", new Gson().toJson(requestData));
        return api.signUpUser(requestData);
    }

}
