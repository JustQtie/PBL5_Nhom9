package com.example.navigationbottom.viewmodel;

import com.example.navigationbottom.response.category.GetCategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApi {
    @GET("api/v1/categories")
    Call<GetCategoryResponse> getListCategory();
}
