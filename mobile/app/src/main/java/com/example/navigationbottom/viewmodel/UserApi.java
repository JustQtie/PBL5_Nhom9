package com.example.navigationbottom.viewmodel;

import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.LoginResponse;
import com.example.navigationbottom.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("api/v1/users/login")
    Call<LoginResponse> postUserLogin(@Body User requestData);

    @POST("api/v1/users/register")
    Call<RegisterResponse> signUpUser(@Body User requestData);
}
