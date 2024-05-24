package com.example.navigationbottom.viewmodel;

import android.content.Context;
import android.util.Log;

import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.user.ChangePassResponse;
import com.example.navigationbottom.response.user.LoginResponse;
import com.example.navigationbottom.response.user.RegisterResponse;
import com.example.navigationbottom.response.user.UserUpdateImageResponse;
import com.google.gson.Gson;

import java.io.IOException;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Path;

public class UserApiService {

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
                .baseUrl(ApiService.BASE_URL)
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

        return api.signUpUser(requestData);
    }
    public Call<User> getUser(@Path("id") Long id){
        return api.getUser(id);
    }

    public Call<ChangePassResponse> changePassword(@Path("id") Long id, @Body ChangePassResponse requestData) {

        return api.changePassword(id, requestData);
    }

    public Call<User> updateUser(@Path("id") Long id, @Body User requestData) {
        Log.d("thao", "update" + new Gson().toJson(requestData));
        return api.updateUser(id, requestData);
    }

    public Call<UserUpdateImageResponse> updateUserImage(Long userId, MultipartBody.Part imageFile) {
        Log.d("hovanthao", "2" + imageFile.toString());
        return api.updateUserImage(userId, imageFile);
    }

}
