package com.example.navigationbottom.viewmodel;

import com.example.navigationbottom.model.Order;
import com.example.navigationbottom.response.book.GetBookResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface OrderApi {
    @POST("api/v1/orders")
    Call<Order> createOrder(@Body Order order);

    @GET("api/v1/orders/booksaving")
    Call<GetBookResponse> getBookSaving();
}
