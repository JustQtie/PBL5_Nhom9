package com.example.navigationbottom.viewmodel;

import com.example.navigationbottom.response.book.BookImageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BookImageApi {
    @GET("api/v1/productimages/thumbnails/{id}")
    Call<List<String>> getThumbnailsByProductId(@Path("id") Long productId);

    @DELETE("api/v1/productimages/thumbnails/{id}")
    Call<BookImageResponse> deleteThumbnails(@Path("id") Long productId);
}
