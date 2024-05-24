package com.example.navigationbottom.viewmodel;

import com.example.navigationbottom.model.Notification;
import com.example.navigationbottom.response.notify.GetNotifyResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotifyApi {
    @POST("api/v1/notifies")
    Call<Notification> createNotify(@Body Notification notification);

    @GET("api/v1/notifies/user/{id}")
    Call<GetNotifyResponse> getNotify(@Path("id") Long id);

    @DELETE("api/v1/notifies/{id}")
    Call<Notification> deleteNotify(@Path("id") Long id);
}
