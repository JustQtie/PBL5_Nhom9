package com.example.navigationbottom.viewmodel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BookImageApi {
    @GET("api/v1/productimages/thumbnails/{id}")
    Call<List<String>> getThumbnailsByProductId(@Path("id") Long productId);
}
