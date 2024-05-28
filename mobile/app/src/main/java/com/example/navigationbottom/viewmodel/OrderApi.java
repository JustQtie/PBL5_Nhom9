package com.example.navigationbottom.viewmodel;

import com.example.navigationbottom.model.Order;
import com.example.navigationbottom.response.book.GetBookResponse;
import com.example.navigationbottom.response.order.GetOrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderApi {
    @POST("api/v1/orders")
    Call<Order> createOrder(@Body Order order);


    @GET("api/v1/orders/user/{id}")
    Call<GetOrderResponse> getOrderByUser(@Path("id") Long userId);

    @GET("api/v1/orders/user_notpaid/{id}")
    Call<GetOrderResponse> getOrderByUserNotPaid(@Path("id") Long userId);

    @PUT("api/v1/orders/{id}")
    Call<Order> updateOrder(@Path("id") Long id, @Body Order order);

    @GET("api/v1/orders/{id}")
    Call<Order> getOrderById(@Path("id") Long id);
}
