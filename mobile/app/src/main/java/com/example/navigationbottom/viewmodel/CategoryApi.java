package com.example.navigationbottom.viewmodel;

import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.response.category.GetCategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoryApi {
    @GET("api/v1/categories")
    Call<GetCategoryResponse> getListCategory();

    @GET("api/v1/categories/{id}")
    Call<Category> getCategoryById(@Path("id") Long id);
}
