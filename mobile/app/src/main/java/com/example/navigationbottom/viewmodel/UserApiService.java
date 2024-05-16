package com.example.navigationbottom.viewmodel;

import android.util.Log;

import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.LoginResponse;
import com.google.gson.Gson;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class UserApiService {
    private static final String BASE_URL = "https://72ea-116-105-167-76.ngrok-free.app/";
    private UserApi api;

    public UserApiService(){
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // cover  laij
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // ep luongg vao
                .build()
                .create(UserApi.class);
    }

    public Call<LoginResponse> postUserLogin(@Body User requestData){
        Log.d("RequestData", new Gson().toJson(requestData));
        return api.postUserLogin(requestData);
    }
}
