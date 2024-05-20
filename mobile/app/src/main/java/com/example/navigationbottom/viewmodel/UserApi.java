package com.example.navigationbottom.viewmodel;

import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.user.LoginResponse;
import com.example.navigationbottom.response.user.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @POST("api/v1/users/login")
    Call<LoginResponse> postUserLogin(@Body User requestData);

    @POST("api/v1/users/register")
    Call<RegisterResponse> signUpUser(@Body User requestData);

    @POST("api/v1/users/{id}")
    Call<User> getUser(@Path("id") Long id);
}
